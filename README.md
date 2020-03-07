```json
{
    "equals": [
        "/someKey", "someValue"
    ]
}
```


```json
{
    "contains": [
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
