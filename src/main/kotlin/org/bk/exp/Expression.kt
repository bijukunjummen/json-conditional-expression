package org.bk.exp

import com.fasterxml.jackson.databind.JsonNode

interface Expression {
    /**
     * Evaluate a target json against the expression
     *
     * @return true if json matches the expression else false
     */
    fun evaluate(node: JsonNode): Boolean

    /**
     * Get the expression as a json
     */
    fun asJsonNode(): JsonNode
}