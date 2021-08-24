package com.exiasoft.its.common.endpoint.rest.mapper

import com.exiasoft.its.common.util.DateTimeUtil
import java.math.BigDecimal
import java.time.Instant
import java.time.OffsetDateTime

class BaseDtoMapper {

    fun asOffsetDateTime(instant: Instant?) : OffsetDateTime? {
        return instant?.let { DateTimeUtil.instant2OffsetDateTime(it) }
    }

    fun asInstant(offsetDateTime: OffsetDateTime?) : Instant? {
        return offsetDateTime?.let { DateTimeUtil.offsetDateTime2Instant(it) }
    }

    fun asBigDecimal(float: Float?) : BigDecimal? {
        return float?.toBigDecimal()
    }
}