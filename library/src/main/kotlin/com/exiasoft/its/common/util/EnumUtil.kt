package com.exiasoft.its.common.util

import com.exiasoft.its.common.enumeration.NumericEnum
import com.exiasoft.its.common.enumeration.StringEnum

class EnumUtil {
    companion object {

        fun <T> enum2Str(value: T?): String? where T : Enum<T>, T : StringEnum {
            return value?.value
        }

        fun <T> str2Enum(c: Class<T>, str: String?): T? where T : Enum<T>, T : StringEnum {
            return str?.let {
                c.enumConstants.find{ v -> v.value == it}
            }
        }

        fun <T> enum2Int(value: T?): Int? where T : Enum<T>, T : NumericEnum {
            return value?.value
        }

        fun <T> int2Enum(c: Class<T>, value: Int?): T? where T : Enum<T>, T : NumericEnum {
            return value?.let {
                c.enumConstants.find{ v -> v.value == it}
            }
        }

    }
}