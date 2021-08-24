package com.exiasoft.its.mktdata.endpoint.rest.mapper

import com.exiasoft.its.common.endpoint.rest.mapper.BaseDtoMapper
import com.exiasoft.its.mktdata.domain.model.MarketPrice
import com.exiasoft.its.mktdata.endpoint.rest.model.MarketPriceDto
import org.mapstruct.Mapper

@Mapper(uses = [BaseDtoMapper::class], componentModel = "spring")
interface MarketPriceDtoMapper {

    fun model2Dto(marketPrice: MarketPrice) : MarketPriceDto

    fun model2Dto(type: MarketPrice.Type) : MarketPriceDto.Type {
        return when (type) {
            MarketPrice.Type.DELAYED -> MarketPriceDto.Type.DELAYED
            MarketPrice.Type.REAL_TIME -> MarketPriceDto.Type.REAL_TIME
        }
    }
}