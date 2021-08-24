package com.exiasoft.its.webapi.endpoint.rest.mapper

import com.exiasoft.its.common.endpoint.rest.mapper.BaseDtoMapper
import com.exiasoft.its.refdata.endpoint.rest.model.Currency
import com.exiasoft.its.webapi.endpoint.rest.model.CurrencyDto
import org.mapstruct.Mapper

@Mapper(uses = [BaseDtoMapper::class], componentModel = "spring")
interface CurrencyDtoMapper {
    fun model2Dto(currency: Currency): CurrencyDto
}