package com.exiasoft.its.common.exception

import org.springframework.http.HttpStatus
import java.lang.Exception

class ValidationException : BaseException {
    constructor(errorCode: String, errorParam: List<String>, errorMessage: String?, exception: Exception?) : super(errorCode, errorParam, errorMessage, exception)
    constructor(errorCode: String) : super(errorCode)
    constructor(errorCode: String, errorMessage: String) : super(errorCode, errorMessage)
    constructor(errorCode: String, errorParam: List<String>) : super(errorCode, errorParam)
    constructor(errorCode: String, errorParam: List<String>, errorMessage: String) : super(errorCode, errorParam, errorMessage)

    fun getHttpStatus(): HttpStatus {
        return messageMap.get(errorCode)?.second ?: HttpStatus.BAD_REQUEST
    }
}