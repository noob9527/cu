package cn.staynoob.cu

import kotlin.reflect.KClass
import kotlin.reflect.KFunction


/**
 * a typical source usually contains three kinds of properties
 *
 * 1. data properties
 * types which can be mapped to database type directly
 * e.g. String, Int, LocalDate
 *
 * 2. entity reference
 * refer to existed entity
 *
 * 3. other request dto
 * this usually happens while corresponding entity property has
 * CascadeType.PERSIST annotation.
 */
interface Cu {

    fun <T : Any> create(
            targetClass: KClass<T>,
            source: Any,
            constructor: KFunction<T>? = null
    ): T

    fun <T : Any> update(targetObj: T, source: Any): T
}