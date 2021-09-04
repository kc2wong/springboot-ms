package com.exiasoft.its.common.entity

import com.exiasoft.its.common.entity.converter.InstantConverter
import com.exiasoft.its.common.entity.converter.StringList2StringConverter
import com.exiasoft.its.common.entity.converter.StringMap2StringConverter
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
abstract class BaseDomainEventEntity {

    companion object  {
        val EVENT_TYPE_CREATE = "CREATE";
        val EVENT_TYPE_UPDATE = "UPDATE";
        val EVENT_TYPE_DELETE = "DELETE";
    }

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

    @Column(name = "DOMAIN_MODEL_ID", length = 100)
    @Convert(converter = StringList2StringConverter::class)
    lateinit var domainModelId: List<String>

    @Column(name = "DOMAIN_MODEL_VERSION")
    var domainModelVersion: Long = 0

    @Column(name = "EVENT_TYPE", length = 20)
    lateinit var eventType: String

    @Column(name = "ADDITIONAL_INFO", length = 1024)
    @Convert(converter = StringMap2StringConverter::class)
    lateinit var additionalInfo: Map<String, String>

}