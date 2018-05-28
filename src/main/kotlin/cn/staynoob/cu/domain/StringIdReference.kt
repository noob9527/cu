package cn.staynoob.cu.domain

import cn.staynoob.cu.resolver.ResourceResolver
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import kotlin.reflect.KClass

@JsonDeserialize(using = StringIdDeserializer::class)
data class StringIdReference(
        override val id: String?
) : IdReference<String> {
    @get: JsonIgnore
    override var resolver: KClass<out ResourceResolver> = super.resolver
        internal set
}