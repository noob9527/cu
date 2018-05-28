package cn.staynoob.cu.domain

import cn.staynoob.cu.resolver.JpaIdResourceResolver
import cn.staynoob.cu.resolver.ResourceResolver
import java.io.Serializable
import kotlin.reflect.KClass

interface IdReference<out T : Serializable> : Reference {
    val id: T?
    override val resolver: KClass<out ResourceResolver>
        get() = JpaIdResourceResolver::class
}

