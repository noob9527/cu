package cn.staynoob.cu.annotation

import cn.staynoob.cu.resolver.JpaIdResourceResolver
import cn.staynoob.cu.resolver.ResourceResolver
import kotlin.reflect.KClass

annotation class ReferenceType(
        val resolverType: KClass<out ResourceResolver> = JpaIdResourceResolver::class
)