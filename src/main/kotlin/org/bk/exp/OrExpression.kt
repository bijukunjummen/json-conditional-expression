package org.bk.exp

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory

data class OrExpression(private val list: List<Expression>) : Expression {
    override fun evaluate(node: JsonNode): Boolean {
        return list.asSequence().any { exp -> exp.evaluate(node) }
    }

    override fun asJsonNode(): JsonNode {
        val listOfExpressionsNode: ArrayNode = JsonNodeFactory.instance.arrayNode()
        list.forEach { expression -> listOfExpressionsNode.add(expression.asJsonNode()) }

        return JsonNodeFactory.instance
            .objectNode()
            .set(Constants.OR, listOfExpressionsNode)
    }

    override fun toString(): String {
        return asJsonNode().toString()
    }
}