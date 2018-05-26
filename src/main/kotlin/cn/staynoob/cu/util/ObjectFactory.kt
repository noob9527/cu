package cn.staynoob.cu.util

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

object ObjectFactory {
    fun <T : Any> create(
            targetClazz: KClass<T>,
            argMap: Map<String, Any?>,
            constructor: KFunction<T>? = null
    ): T {
        if (constructor == null) throw Exception()
        val paramMap = constructor.parameters
                .mapIndexed { i, it -> it.name to i }
                .toMap()

        val args = argMap
                .toList()
                .sortedWith(compareBy { paramMap[it.first] })
                .map { it.second }
                .toTypedArray()

        return constructor.call(*args)
    }
}