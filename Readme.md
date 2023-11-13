# Trim Indent

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.bennyhuo.kotlin/trimindent-gradle-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.bennyhuo.kotlin/trimindent-gradle-plugin)

This is a Kotlin compiler plugin for a compile-time indent trim of raw String. It is pretty useful for String templates with multiline String variables.

## Examples

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

This is because the computation of common indent size is based on the runtime String value which also contains the new lines of `s`.

With this plugin installed, the runtime value of String template variables are ignored, and only the white spaces of the String literal will be removed so that `s2` will be like:

```
hello
world
!!!
hello2
world2
!!!
```

With version 1.7.10.3, you can also do this magic:

```
val s1 = """
    if(a > 1) {
        return true
    }
""".trimIndent()
val s2 = """
    def test(a) {
        $s1
    }
""".trimIndent()

val s3 = """
    class Test {
        $s2
    }
""".trimIndent()
```

The result value of s3 will be:

```
class Test {
    def test(a) {
        if(a > 1) {
            return true
        }
    }
}
```

It is really useful when you use Kotlin String template as a template to render some other Strings.

## Try it

```
plugins {
    ...
    id("com.bennyhuo.kotlin.trimindent") version "<latest-version>"
}
```

## Change Log

See [releases](ttps://github.com/bennyhuo/Kotlin-Trim-Indent/releases).
