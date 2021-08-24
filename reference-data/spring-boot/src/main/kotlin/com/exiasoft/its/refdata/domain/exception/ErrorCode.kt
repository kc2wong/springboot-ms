package com.exiasoft.its.refdata.domain.exception

import com.exiasoft.its.common.exception.ErrorMessage
import com.exiasoft.its.common.exception.ErrorMessageProvider
import org.springframework.http.HttpStatus


class ErrorCode: ErrorMessageProvider {
    companion object {

        @ErrorMessage(message = "Currency with code %s already exists", httpStatus = HttpStatus.BAD_REQUEST)
        const val CURRENCY_ALREADY_EXISTS = "REFDATA.00001"

        @ErrorMessage(message = "Currency with code %s is not found", httpStatus = HttpStatus.NOT_FOUND)
        const val CURRENCY_NOT_FOUND = "REFDATA.00002"

        @ErrorMessage(message = "Sort criteria %s is invalid", httpStatus = HttpStatus.BAD_REQUEST)
        const val INVALID_SORT_CRITERIA = "REFDATA.00003"

        @ErrorMessage(message = "Missing %s name or shortname", httpStatus = HttpStatus.BAD_REQUEST)
        const val MISSING_ENGLISH_NAME = "REFDATA.00003"

    }
}