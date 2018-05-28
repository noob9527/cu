package cn.staynoob.cu.domain

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.node.TextNode

class StringIdDeserializer
    : StdDeserializer<StringIdReference>(StringIdReference::class.java) {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): StringIdReference {
        val node = p.codec.readTree<JsonNode>(p)

        val strNode = when {
            node is TextNode -> node
            node["id"] is TextNode -> node["id"] as TextNode
            else -> throw IllegalArgumentException("unable to deserialize json node: $node")
        }

        val id = strNode.textValue()
        return StringIdReference(id)
    }
}