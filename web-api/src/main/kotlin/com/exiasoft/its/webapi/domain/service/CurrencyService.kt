package com.exiasoft.its.webapi.domain.service

import com.exiasoft.its.refdata.endpoint.rest.api.CurrencyApi
import com.exiasoft.its.refdata.endpoint.rest.model.Currency
import mu.KotlinLogging
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class CurrencyService(
    private val currencyApi: CurrencyApi
) {
    @Cacheable("currency")
    fun getCurrency(currencyCode: String): Currency {
        logger.info { "Get currency for currencyCode = $currencyCode from ref-data" }
        return currencyApi.getCurrency(currencyCode)
    }

    @CacheEvict("currency")
    fun evictCurrencyCache(currencyCode: String) {
        logger.info { "Evict currency cache for currencyCode = $currencyCode" }
    }

}