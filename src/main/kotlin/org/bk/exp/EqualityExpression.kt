package org.bk.exp

import com.fasterxml.jackson.databind.JsonNode

abstract class EqualityExpression(elements: List<JsonNode>) : Expression {
    protected val jsonPointerKey: String

    init {
        if (elements.size != 2) {
            throw ExpressionParseException("equal operator expects a key and a value")
        }

        jsonPointerKey = elements[0].asText()
    }

    companion object {
        fun generate(elements: List<JsonNode>): EqualityExpression {
            if (elements.size != 2) {
                throw ExpressionParseException("equal operator expects a key and a value")
            }

            if (elements[1].isTextual) {
                return StringEqualityExpression(elements)
            }

            if (elements[1].isNumber) {
                return NumericEqualityExpression(elements)
            }
            throw ExpressionParseException("Unknown element type - ${elements[1].nodeType}")
        }
    }
}
