package com.exiasoft.its.refdata.domain.mapper

import com.exiasoft.its.common.domain.model.Locale
import com.exiasoft.its.common.util.StringUtil
import com.exiasoft.its.common.util.StringUtil.Companion.EMPTY_STRING
import com.exiasoft.its.refdata.domain.model.Currency
import com.exiasoft.its.refdata.repository.entity.CurrencyEntity
import com.exiasoft.its.refdata.repository.entity.CurrencyLocaleEntity
import org.mapstruct.*


@Mapper(componentModel = "spring")
interface CurrencyMapper {

    @Mappings(value = [
        Mapping(target = "name", expression = "java(Companion.toNameDomain(currencyEntity.getLocale(), currencyEntity.getName()))"),
        Mapping(target = "shortName", expression = "java(Companion.toShortNameDomain(currencyEntity.getLocale(), currencyEntity.getShortName()))")
    ])
    fun entity2Model(currencyEntity: CurrencyEntity) : Currency

    @Mappings(value = [
        Mapping(target = "id", ignore = true),
        Mapping(target = "createdBy", ignore = true),
        Mapping(target = "createdDateTime", ignore = true),
        Mapping(target = "updatedBy", ignore = true),
        Mapping(target = "updatedDateTime", ignore = true),
        Mapping(target = "version", ignore = true),
        Mapping(target = "name", expression = "java(Companion.toEntityName(currency.getName(), currency.getShortName()))"),
        Mapping(target = "shortName", expression = "java(Companion.toEntityShortName(currency.getName(), currency.getShortName()))"),
        Mapping(target = "locale", expression = "java(Companion.toLocaleEntity(currency.getName(), currency.getShortName()))")
    ])
    fun model2Entity(currency: Currency) : CurrencyEntity

    @Mappings(value = [
        Mapping(target = "id", ignore = true),
        Mapping(target = "code", ignore = true),
        Mapping(target = "name", expression = "java(Companion.toEntityName(currency.getName(), currency.getShortName()))"),
        Mapping(target = "shortName", expression = "java(Companion.toEntityShortName(currency.getName(), currency.getShortName()))"),
        Mapping(target = "locale", expression = "java(Companion.toLocaleEntity(currencyEntity.getLocale(), currency.getName(), currency.getShortName()))"),
        Mapping(target = "createdBy", ignore = true),
        Mapping(target = "createdDateTime", ignore = true),
        Mapping(target = "updatedBy", ignore = true),
        Mapping(target = "updatedDateTime", ignore = true)
    ])
    fun model2Entity(currency: Currency, @MappingTarget currencyEntity: CurrencyEntity)

    @Mappings(value = [
        Mapping(target = "createdBy", ignore = true),
        Mapping(target = "createdDateTime", ignore = true),
        Mapping(target = "updatedBy", ignore = true),
        Mapping(target = "updatedDateTime", ignore = true),
        Mapping(target = "version", ignore = true)
    ])
    fun createDomainFromRequest(request: CreateCurrencyRequest) : Currency

//    @Mappings(value = [
//        Mapping(target = "createdBy", ignore = true),
//        Mapping(target = "createdDateTime", ignore = true),
//        Mapping(target = "updatedBy", ignore = true),
//        Mapping(target = "updatedDateTime", ignore = true),
//    ])
    fun createModelFromRequest(code: String, decimalPlace: Int, shortName: Map<Locale, String>, name: Map<Locale, String>, version: Long) : Currency {
        val rtn = createDomainFromRequest(CreateCurrencyRequest(code, decimalPlace, shortName, name))
        rtn.version = version
        return rtn
    }

    @Mappings
    fun cloneModel(currency: Currency) : Currency

    companion object {

        fun toLocaleEntity(name: Map<Locale, String>, shortName: Map<Locale, String>): List<CurrencyLocaleEntity> {
            return toLocaleEntity(emptyList(), name, shortName)
        }

        fun toLocaleEntity(currencyLocaleEntity: List<CurrencyLocaleEntity>, name: Map<Locale, String>, shortName: Map<Locale, String>): List<CurrencyLocaleEntity> {
            return (name.keys union shortName.keys).filter { it != Locale.EN }.map {
                val currencyLocaleEntity = currencyLocaleEntity.find { localeEntity -> localeEntity.locale == it } ?: CurrencyLocaleEntity()
                currencyLocaleEntity.locale = it
                currencyLocaleEntity.name = name[it] ?: ""
                currencyLocaleEntity.shortName = shortName[it] ?: ""
                currencyLocaleEntity
            }.toList()
        }

        fun toEntityName(name: Map<Locale, String>, shortName: Map<Locale, String>): String {
            return name.getOrDefault(Locale.EN, shortName.getOrDefault(Locale.EN, EMPTY_STRING))
        }

        fun toEntityShortName(name: Map<Locale, String>, shortName: Map<Locale, String>): String {
            return shortName.getOrDefault(Locale.EN, StringUtil.ensureMaxLength(name.getOrDefault(Locale.EN, EMPTY_STRING), 10))
        }

        fun toNameDomain(locale: List<CurrencyLocaleEntity>, enName: String): Map<Locale, String> {
            return locale.map {
                it.locale to it.name
            }.toMap().plus(Pair(Locale.EN, enName))
        }

        fun toShortNameDomain(locale: List<CurrencyLocaleEntity>, enShortName: String): Map<Locale, String> {
            return locale.map {
                it.locale to it.shortName
            }.toMap().plus(Pair(Locale.EN, enShortName))
        }

    }


    data class CreateCurrencyRequest(
        val code: String,
        val decimalPlace: Int,
        var shortName: Map<Locale, String>,
        var name: Map<Locale, String>
    );

}