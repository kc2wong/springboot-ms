package com.exiasoft.its.refdata.endpoint.rest.api

import com.exiasoft.its.common.exception.ValidationException
import com.exiasoft.its.refdata.domain.exception.ErrorCode
import com.exiasoft.its.refdata.domain.service.CurrencyService
import com.exiasoft.its.refdata.endpoint.rest.mapper.CurrencyDtoMapper
import com.exiasoft.its.refdata.endpoint.rest.mapper.toNameModel
import com.exiasoft.its.refdata.endpoint.rest.mapper.toShortNameModel
import com.exiasoft.its.refdata.endpoint.rest.model.CreateCurrencyRequestDto
import com.exiasoft.its.refdata.endpoint.rest.model.CurrencyDto
import com.exiasoft.its.refdata.endpoint.rest.model.PageOfCurrencyDto
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import kotlin.streams.toList

private val logger = KotlinLogging.logger {}

@Component
class CurrencyApiDelegateImpl(
    @Value("\${spring.application.pageSize.default}") val defaultPageSize: Int,
    @Autowired val currencyService: CurrencyService,
    @Autowired val mapper: CurrencyDtoMapper
) : CurrencyApiDelegate {

    override fun createCurrency(createCurrencyRequestDto: CreateCurrencyRequestDto): ResponseEntity<CurrencyDto> {
        logger.info { "createCurrency request = $createCurrencyRequestDto" }
        val newCurrency = currencyService.createCurrency(createCurrencyRequestDto.currencyCode, createCurrencyRequestDto.decimalPlace,
            mapper.toShortNameModel(createCurrencyRequestDto.shortName),
            mapper.toNameModel(createCurrencyRequestDto.name)
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.domain2Dto(newCurrency))
    }

    override fun findCurrency(
        currencyCode: String?,
        name: String?,
        shortName: String?,
        offset: Int,
        limit: Int,
        sort: String?
    ): ResponseEntity<PageOfCurrencyDto> {
        logger.info { "findCurrency currencyCode = $currencyCode, name = $name, shortName = $shortName" }

        val searchResult = currencyService.findCurrency(currencyCode, name, shortName, offset, limit, sort)
        val start = searchResult.pageable.offset.toInt()
        val end = start + searchResult.content.size
        val response = PageOfCurrencyDto(
            start = start,
            end = end,
            total = searchResult.totalElements.toInt(),
            data = searchResult.get().map { mapper.domain2Dto(it) }.toList()
        )
        return ResponseEntity.ok().body(response)
    }

    override fun getCurrency(currencyCode: String): ResponseEntity<CurrencyDto> {
        logger.info { "getCurrency currencyCode = $currencyCode" }

        return currencyService.getCurrency(currencyCode) ?.let {
            ResponseEntity.ok().body(mapper.domain2Dto(it))
        } ?: throw ValidationException(ErrorCode.CURRENCY_NOT_FOUND, listOf(currencyCode))
    }

    override fun putCurrency(currencyCode: String, currencyDto: CurrencyDto): ResponseEntity<CurrencyDto> {
        logger.info { "putCurrency currencyCode = $currencyCode, currencyDto = $currencyDto" }

        val updatedCurrency = currencyService.updateCurrency(currencyCode, mapper.dto2Domain(currencyDto))
        return ResponseEntity.ok(mapper.domain2Dto(updatedCurrency))
    }
}