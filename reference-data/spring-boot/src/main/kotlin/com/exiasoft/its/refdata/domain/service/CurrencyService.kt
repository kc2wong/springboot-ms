package com.exiasoft.its.refdata.domain.service

import com.exiasoft.its.common.domain.event.DomainEvent
import com.exiasoft.its.common.domain.model.Locale
import com.exiasoft.its.common.entity.SearchCriteria
import com.exiasoft.its.common.exception.ValidationException
import com.exiasoft.its.common.util.JpaUtil
import com.exiasoft.its.common.util.StringUtil
import com.exiasoft.its.common.util.StringUtil.Companion.EMPTY_STRING
import com.exiasoft.its.refdata.domain.exception.ErrorCode.Companion.CURRENCY_ALREADY_EXISTS
import com.exiasoft.its.refdata.domain.exception.ErrorCode.Companion.CURRENCY_NOT_FOUND
import com.exiasoft.its.refdata.domain.exception.ErrorCode.Companion.INVALID_SORT_CRITERIA
import com.exiasoft.its.refdata.domain.exception.ErrorCode.Companion.MISSING_ENGLISH_NAME
import com.exiasoft.its.refdata.domain.mapper.CurrencyMapper
import com.exiasoft.its.refdata.domain.model.Currency
import com.exiasoft.its.refdata.repository.CurrencyRepository
import com.exiasoft.its.refdata.repository.entity.CurrencyEntity
import com.exiasoft.its.refdata.repository.entity.CurrencyLocaleEntity
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.event.TransactionalEventListener
import javax.transaction.Transactional


@Service
class CurrencyService(
    val currencyRepository: CurrencyRepository,
    val mapper: CurrencyMapper,
    val kafkaTemplate: KafkaTemplate<String, DomainEvent>,
    val applicationEventPublisher: ApplicationEventPublisher
) {

    private val logger = LoggerFactory.getLogger(CurrencyService::class.java)

    @Transactional
    fun createCurrency(code: String, decimalPlace: Int, shortName: Map<Locale, String>, name: Map<Locale, String>): Currency {
        if (!shortName.containsKey(Locale.EN) || !name.containsKey(Locale.EN)) {
            throw ValidationException(MISSING_ENGLISH_NAME, listOf(Locale.EN.value))
        }
        currencyRepository.findByCode(code) ?.let {
            throw ValidationException(CURRENCY_ALREADY_EXISTS, listOf(code))
        }

        val currency = mapper.createModelFromRequest(code, decimalPlace, shortName, name)
        enrichName(currency.name, currency.shortName).let {
            currency.name = it.first
            currency.shortName = it.second
        }

        val currencyEntity = mapper.model2Entity(currency)
        currencyEntity.locale.forEach {
            it.currency = currencyEntity
        }

        val savedEntity = currencyRepository.save(currencyEntity)
        val rtn = mapper.entity2Model(savedEntity)
        applicationEventPublisher.publishEvent(DomainEvent(
            domainModelName = Currency::class.simpleName!!,
            eventType = DomainEvent.EVENT_TYPE_CREATE,
            domainModelId = listOf(code)))
        return rtn;
    }

    @Transactional
    fun updateCurrency(currencyCode: String, currency: Currency): Currency {
        val currencyEntity = currencyRepository.findByCode(currencyCode)
            ?: throw ValidationException(CURRENCY_NOT_FOUND, listOf(currencyCode))

        // Clone input currency for name enrichment
        val newCurrency = mapper.createModelFromRequest(currencyCode, currency.decimalPlace, currency.shortName, currency.name)
        enrichName(newCurrency.name, newCurrency.shortName).let {
            newCurrency.name = it.first
            newCurrency.shortName = it.second
        }

        // Map domain object to entity
        mapper.model2Entity(newCurrency, currencyEntity)
        with(currencyEntity) {
            // Remove locales that are not found in new image
            val newLocales = newCurrency.name.keys
            this.locale
                .filter { !newLocales.contains(it.locale) }
                .forEach {
                    it.currency = null
                    this.locale.remove(it)
                }

            this.locale.forEach {
                it.currency = this
            }
        }

        // save entity to db
        val savedEntity = currencyRepository.save(currencyEntity)
        val rtn = mapper.entity2Model(savedEntity)
        applicationEventPublisher.publishEvent(DomainEvent(
            domainModelName = Currency::class.simpleName!!,
            eventType = DomainEvent.EVENT_TYPE_UPDATE,
            domainModelId = listOf(currencyCode)))
        return rtn;

    }

    fun getCurrency(code: String): Currency? {
        return currencyRepository.findByCode(code)?.let {
            mapper.entity2Model(it)
        }
    }

    fun findCurrency(code: String?, name: String?, shortName: String?, offset: Int?, limit: Int, sort: String?): Page<Currency> {
        logger.info("findCurrency(), code = {}, name = {}, sortName = {}", code, name, shortName)
        val validSortCriteria = setOf("code", "name", "shortName", "createdBy", "createdDateTime", "updatedBy", "updatedDateTime")
        var sortCriteria = JpaUtil.toSortCriteria(sort ?: "code")
        for (idx in 0 until sortCriteria.count()) {
            with(sortCriteria.elementAt(idx).property) {
                if (!validSortCriteria.contains(this)) {
                    throw ValidationException(INVALID_SORT_CRITERIA, listOf(this))
                }
            }
        }
        // Append default sort criteria
        if (sortCriteria.getOrderFor("code") == null) {
            sortCriteria = sortCriteria.and(Sort.by("code").ascending())
        }

        val specification = mutableListOf<Specification<CurrencyEntity>>()
        code ?.let {
            specification.add(JpaUtil.withCriteria<CurrencyEntity, String>(SearchCriteria("code", SearchCriteria.Condition.EQUAL, it)))
        }
        name ?.let {
            specification.add(JpaUtil.or(listOf(
                JpaUtil.withCriteria<CurrencyEntity, String>(SearchCriteria("name", SearchCriteria.Condition.LIKE, it)),
                JpaUtil.withOuterJoinCriteria<CurrencyEntity, CurrencyLocaleEntity, String>("locale", SearchCriteria("name", SearchCriteria.Condition.LIKE, it))
            )))
        }
        shortName ?.let {
            specification.add(JpaUtil.or(listOf(
                JpaUtil.withCriteria<CurrencyEntity, String>(SearchCriteria("shortName", SearchCriteria.Condition.LIKE, it)),
                JpaUtil.withOuterJoinCriteria<CurrencyEntity, CurrencyLocaleEntity, String>("locale", SearchCriteria("shortName", SearchCriteria.Condition.LIKE, it))
            )))
        }

        val pageRequest = PageRequest.of(offset?:0 / limit, limit, sortCriteria)
        val searchResult = if (specification.isEmpty()) currencyRepository.findAll(pageRequest) else currencyRepository.findAll(
            JpaUtil.and(specification), pageRequest)
        val detailMap = currencyRepository.findByIdIn(searchResult.content.map { it.id }.toList())
            .map { it.id to it }.toMap()

        return searchResult.map {
            mapper.entity2Model(detailMap[it.id] ?: it)
        }
    }

    private fun enrichName(nameMap: Map<Locale, String>, shortNameMap: Map<Locale, String>) : Pair<Map<Locale, String>, Map<Locale, String>> {
        val languages = nameMap.keys union shortNameMap.keys
        val newNameMap = languages.map {
            it to (nameMap[it] ?: shortNameMap[it] ?: EMPTY_STRING)
        }.toMap()
        val newShortNameMap = languages.map {
            it to (shortNameMap[it] ?: (StringUtil.ensureMaxLength(shortNameMap[it] ?: EMPTY_STRING, 10)))
        }.toMap()
        return Pair(newNameMap, newShortNameMap)
    }

    @TransactionalEventListener
    fun doAfterCommit(event: DomainEvent) {
        // Publish event to notify there is change in currency
        logger.info("Publish domainEvent $event")
        kafkaTemplate.send("currency_event", event)     //process here
    }
}