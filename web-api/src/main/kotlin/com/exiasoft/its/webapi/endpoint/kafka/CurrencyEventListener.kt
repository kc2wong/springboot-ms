package com.exiasoft.its.webapi.endpoint.kafka

import com.exiasoft.its.common.domain.event.DomainEvent
import com.exiasoft.its.webapi.domain.service.CurrencyService
import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class CurrencyEventListener(
    val currencyService: CurrencyService
) {

    @KafkaListener(topics = ["currency_event"], groupId = "\${spring.application.name}", containerFactory = "domainEventListenerContainerFactory")
    fun receiveCurrencyEvent(message: DomainEvent) {
        logger.info { "Received message: $message" }
        val cacheEvictEventType = setOf(DomainEvent.EVENT_TYPE_UPDATE, DomainEvent.EVENT_TYPE_DELETE)
        if (message.domainModelName == "Currency" && cacheEvictEventType.contains(message.eventType)) {
            currencyService.evictCurrencyCache(message.domainModelId[0])
        }
    }
}