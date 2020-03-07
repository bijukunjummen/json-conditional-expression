package org.bk.exp

import com.fasterxml.jackson.databind.JsonNode

data class OrExpression(private val list: List<Expression>) : Expression {
    override fun evaluate(node: JsonNode): Boolean {
        return list.asSequence().any { exp -> exp.evaluate(node) }
    }

}