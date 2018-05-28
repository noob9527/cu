package cn.staynoob.cu.domain

import cn.staynoob.cu.resolver.ResourceResolver
import kotlin.reflect.KClass

interface Reference {

    /**
     * if return null
     * cu lib will try to figure out the class via reflection
     */
    fun getReferenceClass(): KClass<out Any>? = null

    val resolver: KClass<out ResourceResolver>
}

