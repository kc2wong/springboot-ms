package com.exiasoft.its.common.domain.converter

import com.exiasoft.its.common.domain.event.DomainEvent
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.apache.kafka.common.serialization.Deserializer
import java.util.*

private val logger = KotlinLogging.logger {}

class DomainEventJsonDeserializer(private val domainModelName: String) :
    Deserializer<DomainEvent> {

    private val mapper = ObjectMapper()
    private val modelIdTypeRef: TypeReference<LinkedList<String>> =
        object : TypeReference<LinkedList<String>>() {}
    private val additionalInfoTypeRef: TypeReference<HashMap<String, String>> =
        object : TypeReference<HashMap<String, String>>() {}

    override fun deserialize(topic: String, data: ByteArray): DomainEvent? {
        logger.debug { "data = ${String(data)}" }
        return mapper.readValue(data, DebeziumTopic::class.java)?.payload?.after?.let {
            logger.debug { "domainModelId = ${it.domainModelId}" }
            logger.debug { "domainModelVersion = ${it.domainModelVersion}" }
            logger.debug { "domainModelName = ${it.domainModelName}" }
            logger.debug { "payload = ${it.additionalInfo}" }
            DomainEvent(domainModelName, mapper.readValue(it.domainModelId, modelIdTypeRef), it.domainModelVersion, it.eventType, mapper.readValue(it.additionalInfo, additionalInfoTypeRef))
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class DebeziumTopic {
        var payload: Payload? = null
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class Payload {
        var after: DomainEventWrapper? = null
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class DomainEventWrapper {
        @JsonProperty("domain_model_name")
        var domainModelName: String = ""
        @JsonProperty("domain_model_id")
        var domainModelId: String? = ""
        @JsonProperty("domain_model_version")
        var domainModelVersion: Long = 0
        @JsonProperty("event_type")
        var eventType: String = ""
        @JsonProperty("additional_info")
        var additionalInfo: String? = ""
    }

}


