package com.exiasoft.its.webapi.endpoint.rest.mapper

import com.exiasoft.its.common.endpoint.rest.mapper.BaseDtoMapper
import com.exiasoft.its.mktdata.endpoint.rest.model.MarketPrice
import com.exiasoft.its.refdata.endpoint.rest.model.Currency
import com.exiasoft.its.webapi.endpoint.rest.model.MarketPriceDto
import org.mapstruct.AfterMapping
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import java.math.BigDecimal
import java.math.RoundingMode

@Mapper(componentModel = "spring")
abstract class MarketPriceMapper: BaseDtoMapper {
    protected abstract fun internalModel2Dto(marketPrice: MarketPrice, currency: Currency): MarketPriceDto

    fun model2Dto(marketPrice: MarketPrice, currency: Currency): MarketPriceDto {
        val rtn = internalModel2Dto(marketPrice, currency)
        val newPrice = rtn.price.setScale(rtn.currency.decimalPlace, RoundingMode.HALF_UP)
        return MarketPriceDto(type = rtn.type, quotationTime = rtn.quotationTime, currency = rtn.currency, price = newPrice)
    }
}