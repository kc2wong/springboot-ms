package com.exiasoft.its.refdata.repository.entity

import com.exiasoft.its.common.entity.BaseEntity
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import javax.persistence.*

@Entity
@NamedEntityGraph(name = "currency.locale",
    attributeNodes = [NamedAttributeNode("locale")]
)
@Table(name = "CURRENCY")
class CurrencyEntity : BaseEntity() {

    @Column(name = "CODE")
    lateinit var code: String

    @Column(name = "DECIMAL_PLACE")
    var decimalPlace: Int = 0

    @Column(name = "NAME")
    lateinit var name: String

    @Column(name = "SHORT_NAME")
    lateinit var shortName: String

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, mappedBy = "currency")
    lateinit var locale: MutableList<CurrencyLocaleEntity>

}