package org.bk.exp

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode


class JsonExpressionEvaluator(
    private val objectMapper: ObjectMapper
) {
    fun matches(expression: String, json: String): Boolean {
        val expressionTree: Expression = parse(expression)
        return expressionTree.evaluate(objectMapper.readTree(json))
    }

    fun parse(expressionJson: String): Expression {
        return traverseAndGenerateTree(objectMapper.readTree(expressionJson))
    }

    private fun traverseAndGenerateTree(expressionJson: JsonNode): Expression {
        if (!expressionJson.isObject) {
            throw ExpressionParseException("Expression root is expected to be a json object")
        }
        return traverseObjectAndGenerateHash(expressionJson as ObjectNode)
    }

    private fun traverseObjectAndGenerateHash(objectNode: ObjectNode): Expression {
        val fieldNames: List<String> = objectNode.fieldNames().asSequence().toList()

        if (fieldNames.size > 1) {
            throw ExpressionParseException("Expected only 1 key to represent an operator, found '$fieldNames'")
        }

        val operator: String = fieldNames[0]

        return generateExpressionFor(operator, objectNode.get(operator))
    }

    private fun generateExpressionFor(operator: String, jsonNode: JsonNode): Expression {
        if (!jsonNode.isArray) {
            throw ExpressionParseException("Expected an array to be passed for evaluating '$operator'")
        }
        val childNodes: List<JsonNode> = (jsonNode as ArrayNode).asSequence().toList()
        return when (operator) {
            Constants.EQUAL ->
                EqualityExpression.generate(childNodes)
            Constants.NOT ->
                NotExpression(traverseAndGenerateTree(childNodes[0]))
            Constants.AND ->
                AndExpression(childNodes.asSequence().map { node -> traverseAndGenerateTree(node) }.toList())
            Constants.OR ->
                OrExpression(childNodes.asSequence().map { node -> traverseAndGenerateTree(node) }.toList())
            Constants.CONTAINS ->
                ContainsExpression(childNodes)
            Constants.CONTAINS_ANY_OF ->
                ContainsAnyOfExpression(childNodes)
            else ->
                throw ExpressionParseException("Unknown operator $operator")
        }
    }
}