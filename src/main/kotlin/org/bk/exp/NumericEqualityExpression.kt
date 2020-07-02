package org.bk.exp

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory

class NumericEqualityExpression(elements: List<JsonNode>) : EqualityExpression(elements) {
    private val value: Number

    init {
        if (!elements[1].isNumber) {
            throw ExpressionParseException("value must be numeric")
        }
        value = elements[1].numberValue()
    }

    override fun evaluate(node: JsonNode): Boolean {
        return node.at(jsonPointerKey).numberValue() == value
    }

    override fun asJsonNode(): JsonNode {
        val equalsArrayNode: ArrayNode = JsonNodeFactory.instance
            .arrayNode()
            .add(jsonPointerKey)
            .add(value.toString())

        return JsonNodeFactory.instance
            .objectNode()
            .set(Constants.EQUAL, equalsArrayNode)
    }

    override fun toString(): String {
        return asJsonNode().toString()
    }
}
