package com.exiasoft.its.mktdata.domain.facade

import com.exiasoft.its.common.exception.ValidationException
import com.exiasoft.its.mktdata.domain.exception.ErrorCode
import com.exiasoft.its.mktdata.domain.model.MarketPrice
import org.springframework.stereotype.Component
import yahoofinance.YahooFinance
import java.time.OffsetDateTime

@Component
class MarketPriceFacade {

    fun getInstrumentMarketPrice(
        marketCode: String,
        instrumentLocalCode: String
    ): MarketPrice {

        if (marketCode != "HKG") {
            throw ValidationException(ErrorCode.INVALID_MARKET, listOf(marketCode))
        }

        if (instrumentLocalCode.length < 4 || instrumentLocalCode.length > 5) {
            throw ValidationException(ErrorCode.INVALID_INSTRUMENT_CODE, listOf("4 OR 5"))
        }

        val symbol = String.format(
            "%s.HK",
            if (instrumentLocalCode.length == 5) instrumentLocalCode.substring(1) else instrumentLocalCode
        )
        return YahooFinance.get(symbol)?.let {
            MarketPrice(
                type = MarketPrice.Type.DELAYED,
                currencyCode = it.currency,
                price = it.quote.price,
                quotationTime = OffsetDateTime.now(),
            )
        } ?: throw ValidationException(ErrorCode.INVALID_INSTRUMENT_CODE, listOf(instrumentLocalCode))

    }
}