package com.exiasoft.its.mktdata

import com.exiasoft.its.common.config.PersistenceConfig
import com.exiasoft.its.common.exception.BaseException
import com.exiasoft.its.mktdata.domain.exception.ErrorCode
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [
    DataSourceAutoConfiguration::class,
    DataSourceTransactionManagerAutoConfiguration::class,
    HibernateJpaAutoConfiguration::class,
    PersistenceConfig::class
])
class Application

fun main(args: Array<String>) {
    BaseException.registerMessageProvider(ErrorCode::class.java)
    runApplication<Application>(*args)
}
