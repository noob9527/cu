package cn.staynoob.cu

import cn.staynoob.cu.exception.ReferenceNotFoundException


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
 * 3. other source
 * this usually happens while corresponding entity property has
 * CascadeType.PERSIST annotation.
 */
interface Cu {

    @Throws(ReferenceNotFoundException::class)
    fun <T : Any> create(
            source: CuSource<T>
    ): T

    @Throws(ReferenceNotFoundException::class)
    fun <T : Any> update(target: T, source: CuSource<T>)
}