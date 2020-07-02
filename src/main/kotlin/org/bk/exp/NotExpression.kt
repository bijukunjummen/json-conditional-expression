package org.bk.exp

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory

class NotExpression(private val expression: Expression) : Expression {

    override fun evaluate(node: JsonNode): Boolean {
        return !expression.evaluate(node)
    }

    override fun asJsonNode(): JsonNode {
        return JsonNodeFactory.instance
            .objectNode()
            .set(
                Constants.NOT,
                JsonNodeFactory.instance
                    .arrayNode()
                    .add(expression.asJsonNode())
            )
    }

    override fun toString(): String {
        return asJsonNode().toString()
    }
}
