package cn.staynoob.cu

import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties

interface CuSource<T : Any> {

    companion object {
        internal val properties =
                CuSource::class.declaredMemberProperties
                        .map { it.name }
    }

    val targetClass: KClass<T>

    fun beforeCreateInstance(arguments: Map<String, Any?>): Map<String, Any?> {
        return arguments
    }

    fun beforeUpdateInstance(instance: T, arguments: Map<String, Any?>): Map<String, Any?> {
        return arguments
    }
}

