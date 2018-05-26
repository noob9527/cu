package cn.staynoob.cu.exception

import cn.staynoob.cu.resolver.ResourceResolver
import cn.staynoob.cu.domain.Reference
import kotlin.reflect.KClass

class UnresolvableReferenceException(
        referenceClass: KClass<out Reference>,
        resolverClass: KClass<out ResourceResolver>
) : RuntimeException("reference class ${referenceClass.simpleName} can't be resolved by resolver: ${resolverClass.simpleName}")