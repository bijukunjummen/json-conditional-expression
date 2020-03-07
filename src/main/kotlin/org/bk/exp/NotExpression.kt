package org.bk.exp

import com.fasterxml.jackson.databind.JsonNode

class NotExpression(private val expression: Expression) : Expression {

    override fun evaluate(node: JsonNode): Boolean {
        return !expression.evaluate(node)
    }

    override fun toString(): String {
        return "not ($expression)"
    }
}
