package com.exiasoft.its.refdata.repository.entity

import com.exiasoft.its.common.entity.BaseDomainEventEntity
import java.util.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "EVT_CURRENCY")
class CurrencyDomainEventEntity() : BaseDomainEventEntity() {

    constructor(eventType: String, domainModelId: List<String>, domainModelVersion: Long) : this() {
        this.id = UUID.randomUUID().toString()
        this.eventType = eventType
        this.domainModelId = domainModelId
        this.domainModelVersion = domainModelVersion
    }

}