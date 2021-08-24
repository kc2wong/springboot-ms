package com.exiasoft.its.common.domain.event

data class DomainEvent(val domainModelName: String, val domainModelId: List<String>, val eventType: String, val payload: Map<String, Any> = emptyMap()) {
    companion object  {
        val EVENT_TYPE_CREATE = "CREATE";
        val EVENT_TYPE_UPDATE = "UPDATE";
        val EVENT_TYPE_DELETE = "DELETE";
    }
}