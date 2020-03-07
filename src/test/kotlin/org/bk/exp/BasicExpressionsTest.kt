package org.bk.exp

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BasicExpressionsTest {

    private val jsonExpressionEvaluator: JsonExpressionEvaluator = JsonExpressionEvaluator(ObjectMapper())

    @Test
    fun simpleEquality() {
        val expr = """
            {
                "equal":["/someKey", "someValue"] 
            }
        """.trimIndent()


        val json: String = """
            {
                "someKey": "someValue"
            }
        """.trimIndent()

        val result: Boolean = jsonExpressionEvaluator.evaluate(expr, json)

        assertThat(result).isTrue()


    }

    @Test
    fun notExpression() {
        val expr = """
            {
                "not": [{"equal":["/someKey", "someValue"]}] 
            }
        """.trimIndent()


        val json1 = """
            {
                "someKey": "someValue"
            }
        """.trimIndent()

        val json2 = """
            {
                "someKey": "someValue2"
            }
        """.trimIndent()

        assertThat(jsonExpressionEvaluator.evaluate(expr, json1)).isFalse()
        assertThat(jsonExpressionEvaluator.evaluate(expr, json2)).isTrue()
    }

    @Test
    fun andExpression() {
        val expr = """
            {
                "and": [
                    {
                        "equal": [
                            "/someKey", "someValue"
                        ]
                    },
                    {
                        "equal": [
                            "/otherKey", "otherValue"
                        ]
                    }
                ]
            }
        """.trimIndent()


        val json1 = """
            {
                "someKey": "someValue",
                "otherKey": "otherValue"
            }
        """.trimIndent()

        val json2 = """
            {
                "someKey": "someValue",
                "otherKey": "otherValue1"
            }
        """.trimIndent()

        assertThat(jsonExpressionEvaluator.evaluate(expr, json1)).isTrue()
        assertThat(jsonExpressionEvaluator.evaluate(expr, json2)).isFalse()
    }

    @Test
    fun orExpression() {
        val expr = """
            {
                "or": [
                    {
                        "equal": [
                            "/someKey", "someValue"
                        ]
                    },
                    {
                        "equal": [
                            "/otherKey", "otherValue"
                        ]
                    }
                ]
            }
        """.trimIndent()


        val json1 = """
            {
                "someKey": "someValue",
                "otherKey": "otherValue"
            }
        """.trimIndent()

        val json2 = """
            {
                "someKey": "someValue",
                "otherKey": "otherValue1"
            }
        """.trimIndent()


        val json3 = """
            {
            }
        """.trimIndent()

        assertThat(jsonExpressionEvaluator.evaluate(expr, json1)).isTrue()
        assertThat(jsonExpressionEvaluator.evaluate(expr, json2)).isTrue()
        assertThat(jsonExpressionEvaluator.evaluate(expr, json3)).isFalse()
    }

    @Test
    fun containsExpression() {
        val expr = """
            {
                "contains": [
                    "/someCollection", ["a", "b"]
                ]
            }
        """.trimIndent()


        val json1 = """
            {
                "someCollection": ["a", "b", "c"],
                "otherKey": "otherValue"
            }
        """.trimIndent()

        val json2 = """
            {
                "someCollection": ["a", "d", "c"],
                "otherKey": "otherValue"
            }
        """.trimIndent()


        assertThat(jsonExpressionEvaluator.evaluate(expr, json1)).isTrue()
        assertThat(jsonExpressionEvaluator.evaluate(expr, json2)).isFalse()
    }

    @Test
    fun notContainsExpression() {
        val expr = """
            {
                "not": [
                    {
                        "contains": [
                            "/someCollection",
                            [
                                "a",
                                "b"
                            ]
                        ]
                    }
                ]
            }
        """.trimIndent()


        val json1 = """
            {
                "someCollection": ["a", "b", "c"],
                "otherKey": "otherValue"
            }
        """.trimIndent()

        val json2 = """
            {
                "someCollection": ["a", "d", "c"],
                "otherKey": "otherValue"
            }
        """.trimIndent()


        assertThat(jsonExpressionEvaluator.evaluate(expr, json1)).isFalse()
        assertThat(jsonExpressionEvaluator.evaluate(expr, json2)).isTrue()
        println(jsonExpressionEvaluator.parse(expr))
    }
}