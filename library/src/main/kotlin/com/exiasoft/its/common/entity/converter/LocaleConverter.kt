package com.exiasoft.its.common.entity.converter

import com.exiasoft.its.common.domain.model.Locale

class LocaleConverter: AbstractEnum2StringConverter<Locale>(Locale::class.java)