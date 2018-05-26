package cn.staynoob.cu.resolver

import cn.staynoob.cu.domain.Reference
import kotlin.reflect.KClass

interface ResourceResolver {
    fun <T : Any> resolve(reference: Reference, targetClass: KClass<T>): T?
}