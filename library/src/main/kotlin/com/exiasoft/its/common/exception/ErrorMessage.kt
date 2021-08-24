package com.exiasoft.its.common.exception

import org.springframework.http.HttpStatus

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ErrorMessage(val message: String, val httpStatus: HttpStatus) {
}