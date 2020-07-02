package org.bk.exp

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory

class ContainsAnyOfExpression(elements: List<JsonNode>) : Expression {
    private val jsonPointerKey: String
    private val values: List<String>

    init {
        if (elements.size != 2) {
            throw ExpressionParseException("containsAnyOf operator expects a key and a value")
        }
        if (!elements[0].isTextual || !elements[1].isArray) {
            throw ExpressionParseException("key expected to be a string and values an array for 'containsAnyOf' operator")
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
                .map { n: JsonNode -> n.asText() }
                .toList()

        return factsList.intersect(values).isNotEmpty()
    }

    override fun asJsonNode(): JsonNode {
        val containsValueArrayNode: ArrayNode = JsonNodeFactory.instance
            .arrayNode()

        values.forEach { value: String -> containsValueArrayNode.add(value) }

        val containsArrayNode: ArrayNode = JsonNodeFactory.instance
            .arrayNode()
            .add(jsonPointerKey)
            .add(containsValueArrayNode)

        return JsonNodeFactory.instance
            .objectNode()
            .set(Constants.CONTAINS_ANY_OF, containsArrayNode)
    }

    override fun toString(): String {
        return asJsonNode().toString()
    }
}