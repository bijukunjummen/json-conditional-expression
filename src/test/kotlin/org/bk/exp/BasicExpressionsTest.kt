package org.bk.exp

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.JsonNodeFactory
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

        val result: Boolean = jsonExpressionEvaluator.matches(expr, json)
        assertThat(result).isTrue()

        assertThat(jsonExpressionEvaluator.parse(expr).asJsonNode())
            .isEqualTo(
                JsonNodeFactory.instance
                    .objectNode()
                    .set(
                        Constants.EQUAL,
                        JsonNodeFactory.instance
                            .arrayNode()
                            .add("/someKey")
                            .add("someValue")
                    )
            )
    }

    @Test
    fun simpleEqualityNumeric() {
        val expr = """
            {
                "equal":["/someKey", 3] 
            }
        """.trimIndent()


        val json: String = """
            {
                "someKey": 3
            }
        """.trimIndent()

        val result: Boolean = jsonExpressionEvaluator.matches(expr, json)
        assertThat(result).isTrue()
        assertThat(jsonExpressionEvaluator.parse(expr).asJsonNode())
            .isEqualTo(
                JsonNodeFactory.instance
                    .objectNode()
                    .set(
                        Constants.EQUAL,
                        JsonNodeFactory.instance
                            .arrayNode()
                            .add("/someKey")
                            .add("3")
                    )
            )
    }

    @Test
    fun numericEqualityInvalidType() {
        val expr = """
            {
                "equal":["/someKey", 3] 
            }
        """.trimIndent()


        val json: String = """
            {
                "someKey": "3"
            }
        """.trimIndent()

        val result: Boolean = jsonExpressionEvaluator.matches(expr, json)

        assertThat(result).isFalse()
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

        assertThat(jsonExpressionEvaluator.matches(expr, json1)).isFalse()
        assertThat(jsonExpressionEvaluator.matches(expr, json2)).isTrue()
        assertThat(jsonExpressionEvaluator.parse(expr).asJsonNode())
            .isEqualTo(
                JsonNodeFactory.instance
                    .objectNode()
                    .set(
                        Constants.NOT,
                        JsonNodeFactory.instance
                            .arrayNode().add(
                                JsonNodeFactory.instance
                                    .objectNode()
                                    .set(
                                        Constants.EQUAL,
                                        JsonNodeFactory.instance
                                            .arrayNode()
                                            .add("/someKey")
                                            .add("someValue")
                                    )
                            )
                    )
            )
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

        assertThat(jsonExpressionEvaluator.matches(expr, json1)).isTrue()
        assertThat(jsonExpressionEvaluator.matches(expr, json2)).isFalse()
        assertThat(jsonExpressionEvaluator.parse(expr).asJsonNode())
            .isEqualTo(
                JsonNodeFactory.instance
                    .objectNode()
                    .set(
                        Constants.AND,
                        JsonNodeFactory.instance
                            .arrayNode()
                            .add(
                                JsonNodeFactory.instance
                                    .objectNode()
                                    .set(
                                        Constants.EQUAL,
                                        JsonNodeFactory.instance
                                            .arrayNode()
                                            .add("/someKey")
                                            .add("someValue")
                                    )
                            )
                            .add(
                                JsonNodeFactory.instance
                                    .objectNode()
                                    .set(
                                        Constants.EQUAL, JsonNodeFactory.instance
                                            .arrayNode()
                                            .add("/otherKey")
                                            .add("otherValue")
                                    )
                            )
                    )
            )
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

        assertThat(jsonExpressionEvaluator.matches(expr, json1)).isTrue()
        assertThat(jsonExpressionEvaluator.matches(expr, json2)).isTrue()
        assertThat(jsonExpressionEvaluator.matches(expr, json3)).isFalse()

        assertThat(jsonExpressionEvaluator.parse(expr).asJsonNode())
            .isEqualTo(
                JsonNodeFactory.instance
                    .objectNode()
                    .set(
                        Constants.OR, JsonNodeFactory.instance
                            .arrayNode()
                            .add(
                                JsonNodeFactory.instance
                                    .objectNode()
                                    .set(
                                        Constants.EQUAL,
                                        JsonNodeFactory.instance
                                            .arrayNode()
                                            .add("/someKey")
                                            .add("someValue")
                                    )
                            )
                            .add(
                                JsonNodeFactory.instance
                                    .objectNode()
                                    .set(
                                        Constants.EQUAL,
                                        JsonNodeFactory.instance
                                            .arrayNode()
                                            .add("/otherKey")
                                            .add("otherValue")
                                    )
                            )
                    )
            )
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

        assertThat(jsonExpressionEvaluator.matches(expr, json1)).isTrue()
        assertThat(jsonExpressionEvaluator.matches(expr, json2)).isFalse()

        assertThat(jsonExpressionEvaluator.parse(expr).asJsonNode())
            .isEqualTo(
                JsonNodeFactory.instance.objectNode()
                    .set(
                        "contains",
                        JsonNodeFactory.instance
                            .arrayNode()
                            .add("/someCollection")
                            .add(
                                JsonNodeFactory.instance
                                    .arrayNode()
                                    .add("a")
                                    .add("b")
                            )
                    )
            )
    }

    @Test
    fun containsAnyOfExpression() {
        val expr = """
            {
                "containsAnyOf": [
                    "/someCollection", ["a", "b"]
                ]
            }
        """.trimIndent()

        val json1 = """
            {
                "someCollection": ["a", "d", "e"],
                "otherKey": "otherValue"
            }
        """.trimIndent()

        val json2 = """
            {
                "someCollection": ["b", "d", "e"],
                "otherKey": "otherValue"
            }
        """.trimIndent()

        val json3 = """
            {
                "someCollection": ["f", "d", "e"],
                "otherKey": "otherValue"
            }
        """.trimIndent()


        assertThat(jsonExpressionEvaluator.matches(expr, json1)).isTrue()
        assertThat(jsonExpressionEvaluator.matches(expr, json2)).isTrue()
        assertThat(jsonExpressionEvaluator.matches(expr, json3)).isFalse()
        assertThat(jsonExpressionEvaluator.parse(expr).asJsonNode())
            .isEqualTo(
                JsonNodeFactory.instance
                    .objectNode()
                    .set(
                        "containsAnyOf",
                        JsonNodeFactory.instance
                            .arrayNode()
                            .add("/someCollection")
                            .add(
                                JsonNodeFactory.instance
                                    .arrayNode()
                                    .add("a")
                                    .add("b")
                            )
                    )
            )
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

        assertThat(jsonExpressionEvaluator.matches(expr, json1)).isFalse()
        assertThat(jsonExpressionEvaluator.matches(expr, json2)).isTrue()
        assertThat(jsonExpressionEvaluator.parse(expr).asJsonNode())
            .isEqualTo(
                JsonNodeFactory.instance.objectNode()
                    .set(
                        "not", JsonNodeFactory.instance.arrayNode().add(
                            JsonNodeFactory.instance.objectNode()
                                .set(
                                    "contains",
                                    JsonNodeFactory.instance.arrayNode()
                                        .add("/someCollection")
                                        .add(
                                            JsonNodeFactory.instance
                                                .arrayNode()
                                                .add("a")
                                                .add("b")
                                        )
                                )
                        )
                    )
            )

    }
}