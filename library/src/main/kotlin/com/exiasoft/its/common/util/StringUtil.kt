package com.exiasoft.its.common.util

class StringUtil {
    companion object {
        const val EMPTY_STRING = ""

        fun ensureMaxLength(str: String, maxLength: Int) : String {
            return if (str.length > maxLength) str.substring(0, maxLength) else str
        }
    }
}