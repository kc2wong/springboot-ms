package com.exiasoft.its.common.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.AuditorAware
import java.util.*
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing(auditorAwareRef="auditorProvider")
class PersistenceConfig {

    @Value("\${spring.application.name:system}")
    lateinit var applicationName: String

    @Bean
    fun auditorProvider(): AuditorAware<String> {
        return AuditorAware<String> {
            Optional.of(applicationName)
        }
    }
}

