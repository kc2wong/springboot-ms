package com.exiasoft.its.common.entity.converter

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import javax.persistence.AttributeConverter

class StringMap2StringConverter(private val objectMapper: ObjectMapper = ObjectMapper()): AttributeConverter<Map<String, String>, String> {

    override fun convertToDatabaseColumn(stringMap: Map<String, String>?): String {
        return objectMapper.writeValueAsString(stringMap.orEmpty())
    }

    override fun convertToEntityAttribute(string: String?): Map<String, String> {
        val typeRef: TypeReference<HashMap<String, String>> = object : TypeReference<HashMap<String, String>>() {}
        return objectMapper.readValue(string ?: "{}", typeRef)
    }
    
}