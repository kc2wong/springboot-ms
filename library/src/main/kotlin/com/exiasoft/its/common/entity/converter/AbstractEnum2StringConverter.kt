package com.exiasoft.its.common.entity.converter

import com.exiasoft.its.common.enumeration.StringEnum
import com.exiasoft.its.common.util.EnumUtil
import javax.persistence.AttributeConverter

abstract class AbstractEnum2StringConverter<T>(private val enumClass: Class<T>) : AttributeConverter<T, String> where T : Enum<T>, T : StringEnum {

    override fun convertToDatabaseColumn(value: T?): String? {
        return EnumUtil.enum2Str(value)
    }

    override fun convertToEntityAttribute(value: String?): T? {
        return EnumUtil.str2Enum(enumClass, value)
    }
}