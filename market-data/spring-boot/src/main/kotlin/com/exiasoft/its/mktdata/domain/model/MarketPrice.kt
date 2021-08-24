package com.exiasoft.its.mktdata.domain.model

data class MarketPrice(
    val type: Type,
    val quotationTime: java.time.OffsetDateTime,
    val price: java.math.BigDecimal,
    val currencyCode: String
) {
    enum class Type(val value: String) {
        REAL_TIME("RealTime"),
        DELAYED("Delayed");
    }
}
