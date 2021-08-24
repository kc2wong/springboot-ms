package com.exiasoft.its.refdata.domain.model

import com.exiasoft.its.common.domain.model.Locale
import java.time.Instant

class Currency {

    lateinit var code: String

    var decimalPlace: Int = 0

    var shortName: Map<Locale, String> = emptyMap()

    var name: Map<Locale, String> = emptyMap()

    lateinit var createdBy: String

    lateinit var createdDateTime: Instant

    lateinit var updatedBy: String

    lateinit var updatedDateTime: Instant

    var version: Long = 0


}
