package com.exiasoft.its.common.util

import com.exiasoft.its.common.entity.SearchCriteria
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.JoinType

class JpaUtil {

    companion object {

        fun <T, R: Comparable<*>> withCriteria(searchCriteria: SearchCriteria<R>): Specification<T> {
            return Specification { root, _, criteriaBuilder ->
                when (searchCriteria.condition) {
                    SearchCriteria.Condition.LIKE -> criteriaBuilder.like(root.get<String>(searchCriteria.name), "%${searchCriteria.value.toString()}%")
                    SearchCriteria.Condition.EQUAL -> criteriaBuilder.equal(root.get<R>(searchCriteria.name), searchCriteria.value)
                }
            }
        }

        fun <P, C, R: Comparable<*>> withInnerJoinCriteria(childPropertyName: String, searchCriteria: SearchCriteria<R>): Specification<P> {
            return Specification { root, _, criteriaBuilder ->
                val join = root.join<P, C>(childPropertyName)
                when (searchCriteria.condition) {
                    SearchCriteria.Condition.LIKE -> criteriaBuilder.like(join.get<String>(searchCriteria.name), "%${searchCriteria.value.toString()}%")
                    SearchCriteria.Condition.EQUAL -> criteriaBuilder.equal(join.get<R>(searchCriteria.name), searchCriteria.value)
                }
            }
        }

        fun <P, C, R: Comparable<*>> withOuterJoinCriteria(childPropertyName: String, searchCriteria: SearchCriteria<R>): Specification<P> {
            return Specification { root, _, criteriaBuilder ->
                val join = root.join<P, C>(childPropertyName, JoinType.LEFT)
                when (searchCriteria.condition) {
                    SearchCriteria.Condition.LIKE -> criteriaBuilder.like(join.get<String>(searchCriteria.name), "%${searchCriteria.value.toString()}%")
                    SearchCriteria.Condition.EQUAL -> criteriaBuilder.equal(join.get<R>(searchCriteria.name), searchCriteria.value)
                }
            }
        }

        fun <T> and(specification: List<Specification<T>>): Specification<T> {
            assert(specification.isNotEmpty())
            var rtn = specification[0]
            for (idx in 1 until specification.size) {
                rtn = rtn.and(specification[idx])
            }
            return rtn
        }

        fun <T> or(specification: List<Specification<T>>): Specification<T> {
            assert(specification.isNotEmpty())
            var rtn = specification[0]
            for (idx in 1 until specification.size) {
                rtn = rtn.or(specification[idx])
            }
            return rtn
        }

        fun toSortCriteria(sort: String): Sort {
            return toSortCriteria(sort, emptyMap())
        }

        fun toSortCriteria(sort: String, fieldMapping: Map<String, String>): Sort {
            val toSort = { str : String ->
                val fieldName = str.substring(1)
                when (str[0]) {
                    '+' -> Sort.by(fieldMapping.getOrDefault(fieldName, fieldName)).ascending()
                    '-' -> Sort.by(fieldMapping.getOrDefault(fieldName, fieldName)).descending()
                    else -> Sort.by(fieldMapping.getOrDefault(str, str)).ascending()
                }
            }
            val token = sort.split(",")
            var rtnSort = toSort(token[0])
            for (idx in 1 until token.size) {
                rtnSort = rtnSort.and(toSort(token[idx]))
            }
            return rtnSort
        }

    }
}