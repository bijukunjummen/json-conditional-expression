package org.bk.exp

import com.fasterxml.jackson.databind.JsonNode

interface Expression {
    fun evaluate(node: JsonNode): Boolean
}