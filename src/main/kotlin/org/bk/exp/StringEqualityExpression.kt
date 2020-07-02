package org.bk.exp

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory

class StringEqualityExpression(elements: List<JsonNode>) : EqualityExpression(elements) {
    private val value: String

    init {
        if (!elements[1].isTextual) {
            throw ExpressionParseException("value must be string")
        }
        value = elements[1].asText()
    }

    override fun evaluate(node: JsonNode): Boolean {
        return node.at(jsonPointerKey).asText() == value
    }

    override fun asJsonNode(): JsonNode {
        val equalsArrayNode: ArrayNode = JsonNodeFactory.instance
            .arrayNode()
            .add(jsonPointerKey)
            .add(value)
        return JsonNodeFactory.instance
            .objectNode()
            .set(Constants.EQUAL, equalsArrayNode)
    }

    override fun toString(): String {
        return asJsonNode().toString()
    }
}
