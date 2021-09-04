package com.exiasoft.its.common.entity.converter

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import java.util.*
import javax.persistence.AttributeConverter

class StringList2StringConverter(private val objectMapper: ObjectMapper = ObjectMapper()): AttributeConverter<List<String>, String> {

    override fun convertToDatabaseColumn(stringList: List<String>?): String {
        return objectMapper.writeValueAsString(stringList.orEmpty())
    }

    override fun convertToEntityAttribute(string: String?): List<String> {
        val typeRef: TypeReference<LinkedList<String>> = object : TypeReference<LinkedList<String>>() {}
        return objectMapper.readValue(string ?: "[]", typeRef)
    }

}