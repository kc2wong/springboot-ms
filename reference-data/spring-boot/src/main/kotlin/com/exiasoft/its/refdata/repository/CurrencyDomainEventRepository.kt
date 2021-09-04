package com.exiasoft.its.refdata.repository

import com.exiasoft.its.refdata.repository.entity.CurrencyDomainEventEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CurrencyDomainEventRepository : JpaRepository<CurrencyDomainEventEntity, String>