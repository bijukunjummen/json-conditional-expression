# Json Expression Evaluator

This is a small Java based library to create and evaluate a json based conditional expression. 

Given a Json which looks like this:

```json
{
    "someKey": "someValue",
    "otherKey": "otherValue"
}
```

and an expression represented the following way:

```json
{
    "equals": [
        "/someKey", "someValue"
    ]
}
```

Running a code of the following type:

```kotlin
JsonExpressionEvaluator.matches(expr, json) //returns true
```


## Details of the Json Expression

The expression follows a very simple pattern, an operator followed by an array of details. It is inspired 
by the [JsonLogic](http://jsonlogic.com/) project.

For eg. 
```json
{
    "equals": [
        "/someKey", "someValue"
    ]
}
```
means that the json should be matched on a Json Pointer called "/someKey" and if that value is "someValue" then the expression is true

Here are a few more examples:
```json
{
    "contains": [
        "/someCollection", ["a", "b"]
    ]
}
```

```json
{
    "containsAnyOf": [
        "/someCollection", ["a", "b"]
    ]
}
```

```json
{
    "not": [
        {
            "equals": [
                "/someKey",
                "someValue"
            ]
        }
    ]
}
```

```json
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
```

```json
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
```

The expression can support fairly complex conditions like the following:
```json
{
    "and": [
        {
            "equal": [
                "/someKey", "someValue"
            ]
        },
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
    ]
}
```
