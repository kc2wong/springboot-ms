package com.exiasoft.its.webapi.config

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair
import java.time.Duration

@Configuration
@EnableCaching
@ConfigurationProperties(prefix = "application")
class CacheConfig {
    lateinit var cache: List<CacheEntryConfig>

    @Bean
    fun redisCacheManagerBuilderCustomizer(): RedisCacheManagerBuilderCustomizer? {
        return RedisCacheManagerBuilderCustomizer { builder: RedisCacheManagerBuilder ->
            cache.forEach {
                builder.withCacheConfiguration(
                    it.name, RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(it.timeToLive))
                        .disableCachingNullValues()
                        .serializeValuesWith(SerializationPair.fromSerializer(JdkSerializationRedisSerializer()))
                )
            }
        }
    }

    class CacheEntryConfig {
        lateinit var name: String
        var timeToLive: Long = 0
    }
}

