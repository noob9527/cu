package cn.staynoob.cu.domain

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.node.IntNode

class IntIdDeserializer
    : StdDeserializer<IntIdReference>(IntIdReference::class.java) {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): IntIdReference {
        val node = p.codec.readTree<JsonNode>(p)

        val intNode = when {
            node is IntNode -> node
            node["id"] is IntNode -> node["id"] as IntNode
            else -> throw IllegalArgumentException("unable to deserialize json node: $node")
        }

        val id = intNode.numberValue() as Int
        return IntIdReference(id)
    }
}