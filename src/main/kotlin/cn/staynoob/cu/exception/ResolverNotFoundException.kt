package cn.staynoob.cu.exception

import cn.staynoob.cu.resolver.ResourceResolver
import kotlin.reflect.KClass

class ResolverNotFoundException(
        resolverClass: KClass<out ResourceResolver>
) : RuntimeException("cannot find a bean of class $resolverClass")
