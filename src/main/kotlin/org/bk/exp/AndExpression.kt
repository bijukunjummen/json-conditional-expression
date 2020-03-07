package org.bk.exp

import com.fasterxml.jackson.databind.JsonNode

data class AndExpression(private val list: List<Expression>) : Expression {
    override fun evaluate(node: JsonNode): Boolean {
        return !list.asSequence().any { exp -> !exp.evaluate(node) }
    }

    override fun toString(): String {
        return list.joinToString(" AND ")
    }
}