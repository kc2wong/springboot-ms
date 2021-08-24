package com.exiasoft.its.webapi.endpoint.rest.api

import com.exiasoft.its.webapi.domain.facade.GetMarketPriceFacade
import com.exiasoft.its.webapi.endpoint.rest.mapper.MarketPriceMapper
import com.exiasoft.its.webapi.endpoint.rest.model.MarketPriceDto
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class MarketPriceApiDelegateImpl(
    private val getMarketPriceFacade: GetMarketPriceFacade,
    private val marketPriceMapper: MarketPriceMapper
) : MarketDataApiDelegate {

    override fun getInstrumentMarketPrice(
        marketCode: String,
        instrumentLocalCode: String
    ): ResponseEntity<MarketPriceDto> {
        logger.info { "getInstrumentMarketPrice() starts, marketCode = $marketCode, instrumentLocalCode = $instrumentLocalCode" }
        val result = getMarketPriceFacade.getInstrumentMarketPrice(marketCode, instrumentLocalCode)
        return ResponseEntity.ok(marketPriceMapper.model2Dto(result.first, result.second))
    }
}