package com.exiasoft.its.common.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import java.util.concurrent.ConcurrentHashMap

abstract class BaseException(val errorCode: String, val errorParam: List<String>, errorMessage: String?, exception: java.lang.Exception?) : Exception(errorMessage, exception) {

    constructor(errorCode: String) : this(errorCode, emptyList(), null, null)

    constructor(errorCode: String, errorMessage: String) : this(errorCode, emptyList(), errorMessage, null)

    constructor(errorCode: String, errorParam: List<String>) : this(errorCode, errorParam, null, null)

    constructor(errorCode: String, errorParam: List<String>, errorMessage: String) : this(errorCode, errorParam, errorMessage, null)

    companion object {
        private val logger = LoggerFactory.getLogger(BaseException::class.java)

        var messageMap : ConcurrentHashMap<String, Pair<String, HttpStatus?>> = ConcurrentHashMap()
        fun registerMessageProvider(vararg classes: Class<*>) {
            classes.forEach { clazz ->
                val classMessageMap = clazz.declaredFields.filter { it.getAnnotation(ErrorMessage::class.java) != null }
                        .map { it.get(null).toString() to with(it.getAnnotation(ErrorMessage::class.java), { Pair(this.message, this.httpStatus) }) }
                        .toMap()
                messageMap.keys.intersect(classMessageMap.keys).let {
                    if (it.isNotEmpty()) {
                        logger.warn("Duplicated error codes $it found in class $clazz")
                    }
                }
                messageMap.putAll(classMessageMap)
            }
        }
    }

    override val message: String
        get() {

            val msg = (messageMap.get(errorCode) ?: Pair(errorCode, "")).first
            return when (errorParam.size) {
                0 -> msg
                1 -> msg.format(errorParam[0])
                2 -> msg.format(errorParam[0], errorParam[1])
                else -> msg.format(errorParam[0], errorParam[1], errorParam[2])
            }
        }
}