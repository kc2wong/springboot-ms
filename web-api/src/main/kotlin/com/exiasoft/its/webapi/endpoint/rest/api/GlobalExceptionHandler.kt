package com.exiasoft.its.webapi.endpoint.rest.api

import com.exiasoft.its.common.exception.BaseException
import com.exiasoft.its.common.exception.ValidationException
import com.exiasoft.its.webapi.endpoint.rest.model.ErrorResponseDto
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [BaseException::class])
    protected fun handleValidationException (
        ex: ValidationException, request: WebRequest?
    ): ResponseEntity<Any> {
        val responseDto = ErrorResponseDto(
            errorCode = ex.errorCode,
            errorMessage = ex.message,
            errorParam = ex.errorParam
        )
        return handleExceptionInternal(
            ex, responseDto,
            HttpHeaders(), ex.getHttpStatus(), request!!
        )
    }
}
