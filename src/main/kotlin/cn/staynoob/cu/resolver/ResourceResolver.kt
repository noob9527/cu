package cn.staynoob.cu.resolver

import cn.staynoob.cu.domain.Reference
import cn.staynoob.cu.exception.ReferenceNotFoundException
import kotlin.reflect.KClass

interface ResourceResolver {
    @Throws(ReferenceNotFoundException::class)
    fun <T : Any> resolve(reference: Reference, targetClass: KClass<T>): T
}