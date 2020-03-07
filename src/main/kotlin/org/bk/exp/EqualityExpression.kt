package org.bk.exp

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode

class EqualityExpression(expressionNode: ArrayNode) : Expression {
    private val jsonPointerKey: String
    private val value: String

    init {
        val elements: List<JsonNode> = expressionNode.asSequence().toList()
        if (elements.size != 2) {
            throw ExpressionParseException("equal operator expects a key and a value")
        }
        if (!elements[0].isTextual || !elements[1].isTextual) {
            throw ExpressionParseException("key and values for equal operator expected to be a string")
        }
        jsonPointerKey = elements[0].asText()
        value = elements[1].asText()
    }

    override fun evaluate(node: JsonNode): Boolean {
        return node.at(jsonPointerKey).asText() == value
    }

    override fun toString(): String {
        return "'$jsonPointerKey'='$value'"
    }


}
