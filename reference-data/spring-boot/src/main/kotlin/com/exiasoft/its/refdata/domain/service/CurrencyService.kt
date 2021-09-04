package com.exiasoft.its.refdata.domain.service

import com.exiasoft.its.common.domain.model.Locale
import com.exiasoft.its.common.entity.BaseDomainEventEntity
import com.exiasoft.its.common.entity.SearchCriteria
import com.exiasoft.its.common.exception.ValidationException
import com.exiasoft.its.common.util.JpaUtil
import com.exiasoft.its.common.util.StringUtil
import com.exiasoft.its.common.util.StringUtil.Companion.EMPTY_STRING
import com.exiasoft.its.refdata.domain.exception.ErrorCode.Companion.CURRENCY_ALREADY_EXISTS
import com.exiasoft.its.refdata.domain.exception.ErrorCode.Companion.CURRENCY_NOT_FOUND
import com.exiasoft.its.refdata.domain.exception.ErrorCode.Companion.INVALID_SORT_CRITERIA
import com.exiasoft.its.refdata.domain.exception.ErrorCode.Companion.INVALID_VERSION_NUMBER
import com.exiasoft.its.refdata.domain.exception.ErrorCode.Companion.MISSING_ENGLISH_NAME
import com.exiasoft.its.refdata.domain.mapper.CurrencyMapper
import com.exiasoft.its.refdata.domain.model.Currency
import com.exiasoft.its.refdata.repository.CurrencyDomainEventRepository
import com.exiasoft.its.refdata.repository.CurrencyRepository
import com.exiasoft.its.refdata.repository.entity.CurrencyDomainEventEntity
import com.exiasoft.its.refdata.repository.entity.CurrencyEntity
import com.exiasoft.its.refdata.repository.entity.CurrencyLocaleEntity
import mu.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import javax.transaction.Transactional

private val logger = KotlinLogging.logger {}

@Service
class CurrencyService(
    val currencyRepository: CurrencyRepository,
    val currencyDomainEventRepository: CurrencyDomainEventRepository,
    val mapper: CurrencyMapper
) {

    @Transactional
    fun createCurrency(currencyCode: String, decimalPlace: Int, shortName: Map<Locale, String>, name: Map<Locale, String>): Currency {
        if (!shortName.containsKey(Locale.EN) || !name.containsKey(Locale.EN)) {
            throw ValidationException(MISSING_ENGLISH_NAME, listOf(Locale.EN.value))
        }
        currencyRepository.findByCode(currencyCode) ?.let {
            throw ValidationException(CURRENCY_ALREADY_EXISTS, listOf(currencyCode))
        }

        val currency = mapper.createModelFromRequest(currencyCode, decimalPlace, shortName, name, 0)
        enrichName(currency.name, currency.shortName).let {
            currency.name = it.first
            currency.shortName = it.second
        }

        val currencyEntity = mapper.model2Entity(currency)
        currencyEntity.locale.forEach {
            it.currency = currencyEntity
        }

        val savedEntity = currencyRepository.save(currencyEntity)
        val domainEvent = CurrencyDomainEventEntity(BaseDomainEventEntity.EVENT_TYPE_CREATE, listOf(currencyEntity.code), currencyEntity.version)
        currencyDomainEventRepository.save(domainEvent)
        return mapper.entity2Model(savedEntity)
    }

    @Transactional
    fun updateCurrency(currencyCode: String, currency: Currency): Currency {
        val currencyEntity = currencyRepository.findByCode(currencyCode)
            ?: throw ValidationException(CURRENCY_NOT_FOUND, listOf(currencyCode))

        if (currency.version != currencyEntity.version) {
            throw ValidationException(INVALID_VERSION_NUMBER, listOf(currency.version.toString(), currencyEntity.version.toString()))
        }

        // Clone input currency for name enrichment
        val newCurrency = mapper.createModelFromRequest(currencyCode, currency.decimalPlace, currency.shortName, currency.name, currency.version)
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
        val domainEvent = CurrencyDomainEventEntity(BaseDomainEventEntity.EVENT_TYPE_UPDATE, listOf(currencyEntity.code), currencyEntity.version)
        currencyDomainEventRepository.save(domainEvent)
        return mapper.entity2Model(savedEntity)
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
}