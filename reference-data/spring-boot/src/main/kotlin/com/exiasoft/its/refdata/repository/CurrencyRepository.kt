package com.exiasoft.its.refdata.repository

import com.exiasoft.its.refdata.repository.entity.CurrencyEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface CurrencyRepository : JpaRepository<CurrencyEntity, String>, JpaSpecificationExecutor<CurrencyEntity> {

    @EntityGraph(value = "currency.locale")
    fun findByCode(code: String) : CurrencyEntity?

    @EntityGraph(value = "currency.locale")
    fun findByIdIn(toList: List<String>): List<CurrencyEntity>
}