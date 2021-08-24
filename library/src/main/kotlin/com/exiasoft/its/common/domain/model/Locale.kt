package com.exiasoft.its.common.domain.model

import com.exiasoft.its.common.enumeration.StringEnum

enum class Locale(override val value: String): StringEnum {
    EN("en"),
    ZH_HANT("zh-Hant"),
    ZH_HANS("zh-Hans");
}