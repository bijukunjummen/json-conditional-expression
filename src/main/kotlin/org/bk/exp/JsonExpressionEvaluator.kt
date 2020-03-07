package org.bk.exp

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode


class JsonExpressionEvaluator(
    private val objectMapper: ObjectMapper
) {
    fun evaluate(expression: String, json: String): Boolean {
        val expressionTree: Expression  =  parse(objectMapper.readTree(expression))
        return expressionTree.evaluate(objectMapper.readTree(json))
    }

    private fun parse(expressionJson: JsonNode): Expression {
        return traverseAndGenerateTree(expressionJson)
    }

    private fun traverseAndGenerateTree(expressionJson: JsonNode): Expression {
        if (!expressionJson.isObject) {
            throw ExpressionParseException("Expression root is expected to be a json object")
        }
        return traverseObjectAndGenerateHash(expressionJson as ObjectNode)
    }

    private fun traverseObjectAndGenerateHash(objectNode: ObjectNode): Expression {
        val fieldNames = objectNode.fieldNames().asSequence().toList()

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
        val arrayNode: ArrayNode = jsonNode as ArrayNode
        return when (operator) {
            EQUAL ->
                EqualityExpression(arrayNode)
            NOT ->
                NotExpression(traverseAndGenerateTree(arrayNode[0]))
            AND ->
                AndExpression(arrayNode.asSequence().map{node -> traverseAndGenerateTree(node)}.toList())
            OR ->
                OrExpression(arrayNode.asSequence().map{node -> traverseAndGenerateTree(node)}.toList())
            CONTAINS ->
                ContainsExpression(arrayNode)
            else ->
                throw ExpressionParseException("Unknown operator $operator")
        }
    }

    companion object {
        const val EQUAL = "equal"
        const val NOT = "not"
        const val AND = "and"
        const val OR = "or"
        const val CONTAINS = "contains"
    }


}