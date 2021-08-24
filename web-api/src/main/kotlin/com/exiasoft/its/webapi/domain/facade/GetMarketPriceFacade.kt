package com.exiasoft.its.webapi.domain.facade

import com.exiasoft.its.mktdata.endpoint.rest.model.MarketPrice
import com.exiasoft.its.refdata.endpoint.rest.model.Currency
import com.exiasoft.its.webapi.domain.service.CurrencyService
import com.exiasoft.its.webapi.domain.service.MarketDataService
import mu.KotlinLogging
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class GetMarketPriceFacade(
    private val currencyService: CurrencyService,
    private val marketDataService: MarketDataService
) {

    fun getInstrumentMarketPrice(
        marketCode: String,
        instrumentLocalCode: String
    ): Pair<MarketPrice, Currency> {
        logger.info { "Receive request with marketCode = $marketCode, instrumentLocalCode = $instrumentLocalCode" }
        val marketPrice = marketDataService.getInstrumentMarketPrice(marketCode, instrumentLocalCode)
        logger.debug { "Get market price success, result = $marketPrice" }
        val currency = currencyService.getCurrency(marketPrice.currencyCode)
        return Pair(marketPrice, currency)
    }
}