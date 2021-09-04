package com.exiasoft.its.webapi.config

import com.exiasoft.its.common.domain.converter.DomainEventJsonDeserializer
import com.exiasoft.its.common.domain.event.DomainEvent
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory

@Configuration
@EnableKafka
class KafkaConfig {

    @Value(value = "\${kafka.bootstrapAddress}")
    private lateinit var bootstrapAddress: String

    @Value(value = "\${spring.application.name}")
    private lateinit var groupId: String

    @Bean
    fun currencyConsumerFactory(): ConsumerFactory<String, DomainEvent> {
        val props = mapOf(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapAddress,
            ConsumerConfig.GROUP_ID_CONFIG to groupId,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to DomainEventJsonDeserializer::class.java
        )
        return DefaultKafkaConsumerFactory(props, StringDeserializer(), DomainEventJsonDeserializer("CURRENCY"))
    }

    @Bean
    fun currencyDomainEventListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, DomainEvent>? {
        val factory = ConcurrentKafkaListenerContainerFactory<String, DomainEvent>()
        factory.consumerFactory = currencyConsumerFactory()
        return factory
    }

}