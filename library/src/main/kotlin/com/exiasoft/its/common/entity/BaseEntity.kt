package com.exiasoft.its.common.entity

import com.exiasoft.its.common.entity.converter.InstantConverter
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.*
import javax.persistence.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",
        strategy = "uuid")
    @Column(name = "RECORD_ID")
    var id: String = UUID.randomUUID().toString()

    @CreatedBy
    @Column(name = "CREATED_BY")
    lateinit var createdBy: String

    @CreatedDate
    @Column(name = "CREATED_DATETIME")
    @Convert(converter = InstantConverter::class)
    lateinit var createdDateTime: Instant

    @LastModifiedBy
    @Column(name = "UPDATED_BY")
    lateinit var updatedBy: String

    @LastModifiedDate
    @Column(name = "UPDATED_DATETIME")
    @Convert(converter = InstantConverter::class)
    lateinit var updatedDateTime: Instant

    @Version
    @Column(name = "VERSION")
    var version: Long = 0
}