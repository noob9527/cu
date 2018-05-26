package cn.staynoob.cu

import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

fun extractPropertyMap(clazz: KClass<*>): Map<String, KProperty1<out Any, Any?>> {
    return clazz.memberProperties
            .map { it.name to it }
            .toMap()
}

fun extractPropertyValueMap(obj: Any): Map<String, Any?> {
    return extractPropertyMap(obj::class)
            .mapValues { it.value.getter.call(obj) }
}
