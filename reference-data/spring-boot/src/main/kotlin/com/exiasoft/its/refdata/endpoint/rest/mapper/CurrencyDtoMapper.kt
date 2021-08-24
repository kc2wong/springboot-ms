package com.exiasoft.its.refdata.endpoint.rest.mapper

import com.exiasoft.its.common.domain.model.Locale
import com.exiasoft.its.common.endpoint.rest.mapper.BaseDtoMapper
import com.exiasoft.its.common.util.EnumUtil
import com.exiasoft.its.refdata.domain.model.Currency
import com.exiasoft.its.refdata.endpoint.rest.model.CreateCurrencyRequestNameDto
import com.exiasoft.its.refdata.endpoint.rest.model.CreateCurrencyRequestShortNameDto
import com.exiasoft.its.refdata.endpoint.rest.model.CurrencyDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(uses = [BaseDtoMapper::class], componentModel = "spring")
interface CurrencyDtoMapper {

    @Mappings(value = [
        Mapping(source = "code", target = "currencyCode"),
        Mapping(target = "name", expression = "java(Companion.toNameDto(currency.getName()))"),
        Mapping(target = "shortName", expression = "java(Companion.toShortNameDto(currency.getShortName()))")
    ])
    fun domain2Dto(currency: Currency) : CurrencyDto

    @Mappings(value = [
        Mapping(source = "currencyCode", target = "code"),
        Mapping(target = "name", expression = "java(Companion.toNameDomain(request.getName()))"),
        Mapping(target = "shortName", expression = "java(Companion.toShortNameDomain(request.getShortName()))"),
        Mapping(target = "createdBy", ignore = true),
        Mapping(target = "createdDateTime", ignore = true),
        Mapping(target = "updatedBy", ignore = true),
        Mapping(target = "updatedDateTime", ignore = true)
    ])
    fun dto2Domain(request: CurrencyDto) : Currency

    companion object {

        fun toNameDto(name: Map<Locale, String>) : List<CreateCurrencyRequestNameDto> {
            return name.map {
                CreateCurrencyRequestNameDto(locale = it.key.value, value = it.value)
            }.toList()
        }

        fun toShortNameDto(name: Map<Locale, String>) : List<CreateCurrencyRequestShortNameDto> {
            return name.map {
                CreateCurrencyRequestShortNameDto(locale = it.key.value, value = it.value)
            }.toList()
        }

        fun toNameDomain(name: List<CreateCurrencyRequestNameDto>) : Map<Locale, String> {
            return name.filter { EnumUtil.str2Enum(Locale::class.java, it.locale) != null }
                    .map {
                        EnumUtil.str2Enum(Locale::class.java, it.locale)!! to it.value
                    }.toMap()
        }

        fun toShortNameDomain(name: List<CreateCurrencyRequestShortNameDto>) : Map<Locale, String> {
            return name.filter { EnumUtil.str2Enum(Locale::class.java, it.locale) != null }
                    .map {
                        EnumUtil.str2Enum(Locale::class.java, it.locale)!! to it.value
                    }.toMap()
        }

    }
}

fun CurrencyDtoMapper.toNameModel(name: List<CreateCurrencyRequestNameDto>) : Map<Locale, String> {
    return name.filter { EnumUtil.str2Enum(Locale::class.java, it.locale) != null }
        .map {
            EnumUtil.str2Enum(Locale::class.java, it.locale)!! to it.value
        }.toMap()
}

fun CurrencyDtoMapper.toShortNameModel(name: List<CreateCurrencyRequestShortNameDto>) : Map<Locale, String> {
    return name.filter { EnumUtil.str2Enum(Locale::class.java, it.locale) != null }
        .map {
            EnumUtil.str2Enum(Locale::class.java, it.locale)!! to it.value
        }.toMap()
}
