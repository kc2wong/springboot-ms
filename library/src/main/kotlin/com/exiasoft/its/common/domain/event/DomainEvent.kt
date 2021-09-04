package com.exiasoft.its.common.domain.event

data class DomainEvent(val domainModelName: String, val domainModelId: List<String>, val domainModelVersion: Long, val eventType: String, val additionalInfo: Map<String, Any> = emptyMap()) {
    companion object  {
        val EVENT_TYPE_CREATE = "CREATE";
        val EVENT_TYPE_UPDATE = "UPDATE";
        val EVENT_TYPE_DELETE = "DELETE";
    }
}