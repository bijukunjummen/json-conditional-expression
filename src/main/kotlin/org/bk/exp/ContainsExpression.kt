package org.bk.exp

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode

class ContainsExpression(elements: List<JsonNode>) : Expression {
    private val jsonPointerKey: String
    private val values: List<String>

    init {
        if (elements.size != 2) {
            throw ExpressionParseException("contains operator expects a key and a value")
        }
        if (!elements[0].isTextual || !elements[1].isArray) {
            throw ExpressionParseException("key expected to be a string and values an array for 'contains' operator")
        }
        jsonPointerKey = elements[0].asText()
        values = (elements[1] as ArrayNode).asSequence().map { node -> node.asText() }.toList()
    }


    override fun evaluate(node: JsonNode): Boolean {
        val jsonNode: JsonNode = node.at(jsonPointerKey)
        if (jsonNode.isMissingNode || !jsonNode.isArray) {
            return false
        }

        val factsList: List<String> =
            (jsonNode as ArrayNode)
                .asSequence()
                .map { n -> n.asText() }
                .toList()

        return factsList.containsAll(values)
    }

    override fun toString(): String {
        return "'$jsonPointerKey' contains '$values')"
    }


}