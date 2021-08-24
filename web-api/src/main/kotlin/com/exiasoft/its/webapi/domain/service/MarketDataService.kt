package com.exiasoft.its.webapi.domain.service

import com.exiasoft.its.mktdata.endpoint.rest.api.MarketDataApi
import com.exiasoft.its.mktdata.endpoint.rest.model.MarketPrice
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@Component
class MarketDataService(
    private val marketDataApi: MarketDataApi
) {
    fun getInstrumentMarketPrice(marketCode: String, instrumentLocalCode: String): MarketPrice {
        return marketDataApi.getInstrumentMarketPrice(marketCode, instrumentLocalCode)
    }
}