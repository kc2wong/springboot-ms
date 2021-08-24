package com.exiasoft.its.common.util

import java.sql.Timestamp
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId

class DateTimeUtil {
    companion object {
        fun currentTime() : Instant {
            return Instant.now()
        }

        fun instant2OffsetDateTime(instant: Instant) : OffsetDateTime {
            return instant2OffsetDateTime(instant, ZoneId.systemDefault())
        }

        fun instant2Timestamp(instant: Instant) : Timestamp {
            return Timestamp(instant.toEpochMilli())
        }

        fun instant2OffsetDateTime(instant: Instant, zoneId: ZoneId) : OffsetDateTime {
            return OffsetDateTime.ofInstant(instant, zoneId);
        }

        fun offsetDateTime2Instant(offsetDateTime: OffsetDateTime) : Instant {
            return offsetDateTime.toInstant()
        }

        fun timestamp2Instant(timestamp: Timestamp): Instant {
            return Instant.ofEpochMilli(timestamp.time)
        }
    }
}