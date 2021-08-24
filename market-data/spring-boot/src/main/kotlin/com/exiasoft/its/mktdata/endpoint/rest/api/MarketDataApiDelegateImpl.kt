package com.exiasoft.its.mktdata.endpoint.rest.api

import com.exiasoft.its.mktdata.domain.facade.MarketPriceFacade
import com.exiasoft.its.mktdata.endpoint.rest.mapper.MarketPriceDtoMapper
import com.exiasoft.its.mktdata.endpoint.rest.model.MarketPriceDto
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class MarketDataApiDelegateImpl(
    val marketPriceFacade: MarketPriceFacade,
    val marketPriceDtoMapper: MarketPriceDtoMapper
) : MarketDataApiDelegate {

    override fun getInstrumentMarketPrice(
        marketCode: String,
        instrumentLocalCode: String
    ): ResponseEntity<MarketPriceDto> {
        logger.debug { "Receive request with marketCode = $marketCode, instrumentLocalCode = $instrumentLocalCode" }
        return marketPriceFacade.getInstrumentMarketPrice(marketCode, instrumentLocalCode).let {
            marketPriceDtoMapper.model2Dto(it)
        }.let {
            ResponseEntity.ok(it)
        }
    }

}