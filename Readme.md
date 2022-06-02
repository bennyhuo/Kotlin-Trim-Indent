# Trim Indent

This is a Kotlin compiler plugin for a compile-time indent trim of raw String. It is pretty useful for String templates with multiline String variables.

## Example

We have two Strings as below:

```kotlin
val s = """
    hello
    world
    !!!
""".trimIndent()

val s2 = """
    $s
    hello2
    world2
    !!!
""".trimIndent()
```

After compilation, we will get:

```kotlin
val s = "\n    hello\n    world\n    !!!\n".trimIndent()
val s2 = "\n    " + s + "\n    hello2\n    world2    !!!\n".trimIndent()
```

No spaces are dropped in the compile-time.

In the runtime, the `trimIndent` function is called. For `s`, the common indent will be removed as expected:

```
hello
world
!!!
```

But for `s2`, things are going to be weird.

```
    hello
world
!!!
    hello2
    world2
    !!!
```

Thi is because the computation of common indent size is based on the runtime String value which also contains the new lines of `s`.

With this plugin installed, the runtime value of String template variables are ignored, and only the white spaces of the String literal will be removed so that `s2` will be like:

```
hello
world
!!!
hello2
world2
!!!
```

## Try it

```
buildscript {
    dependencies {
        classpath("com.bennyhuo.kotlin:trimindent-gradle-plugin:1.6.10.0")
    }
}

plugins {
    ...
    id("com.bennyhuo.kotlin.trimindent")
}
```