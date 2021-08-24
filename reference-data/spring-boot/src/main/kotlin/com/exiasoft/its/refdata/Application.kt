package com.exiasoft.its.refdata

import com.exiasoft.its.common.exception.BaseException
import com.exiasoft.its.refdata.domain.exception.ErrorCode
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    BaseException.registerMessageProvider(ErrorCode::class.java)
    runApplication<Application>(*args)
}
