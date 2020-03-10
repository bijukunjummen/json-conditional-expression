package org.bk.exp

import com.fasterxml.jackson.databind.JsonNode

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

    override fun toString(): String {
        return "'$jsonPointerKey'='$value'"
    }


}
