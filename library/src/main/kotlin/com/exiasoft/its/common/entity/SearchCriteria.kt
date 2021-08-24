package com.exiasoft.its.common.entity

class SearchCriteria<T: Comparable<*>>(val name: String, val condition: Condition, val value: T) {
    enum class Condition {
        EQUAL,
        LIKE
    }
}
