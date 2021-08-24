package com.exiasoft.its.common.entity.converter

import com.exiasoft.its.common.util.DateTimeUtil
import java.sql.Timestamp
import java.time.Instant
import javax.persistence.AttributeConverter

class InstantConverter: AttributeConverter<Instant, Timestamp> {

    override fun convertToDatabaseColumn(instant: Instant?): Timestamp? {
        return instant?.let { DateTimeUtil.instant2Timestamp(it) }
    }

    override fun convertToEntityAttribute(timestamp: Timestamp?): Instant? {
        return timestamp?.let { DateTimeUtil.timestamp2Instant(it) }
    }
}