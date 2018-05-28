package cn.staynoob.cu.domain

import cn.staynoob.cu.resolver.ResourceResolver
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import kotlin.reflect.KClass

@JsonDeserialize(using = IntIdDeserializer::class)
data class IntIdReference(
        override val id: Int?
) : IdReference<Int> {
    @get: JsonIgnore
    override var resolver: KClass<out ResourceResolver> = super.resolver
        internal set
}