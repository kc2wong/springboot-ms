package com.exiasoft.its.refdata.repository.entity

import com.exiasoft.its.common.domain.model.Locale
import com.exiasoft.its.common.entity.BaseEntity
import com.exiasoft.its.common.entity.converter.LocaleConverter
import javax.persistence.*

@Entity
@Table(name = "CURRENCY_LOCALE")
class CurrencyLocaleEntity : BaseEntity() {

    @Column(name = "LOCALE")
    @Convert(converter = LocaleConverter::class)
    lateinit var locale: Locale

    @Column(name = "SHORT_NAME")
    lateinit var shortName: String

    @Column(name = "NAME")
    lateinit var name: String

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id", nullable = false)
    var currency: CurrencyEntity? = null

}