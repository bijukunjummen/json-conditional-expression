package org.bk.exp

import com.fasterxml.jackson.databind.JsonNode

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

    override fun toString(): String {
        return "'$jsonPointerKey'=$value"
    }


}
