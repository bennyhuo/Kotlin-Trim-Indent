// SOURCE
// FILE: Main.kt [MainKt#main]
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

val string = """
  items:
    - 1
    - 2
    - 3
""".trimIndent()

val items = listOf(1, 2, 3)
val string2 = """
  items:
  ${items.joinToString("\n") { "  - $it" }}
""".trimIndent()

fun main() {
    println(s)
    println(s2)
    println(string)
    println(string2)
}

// GENERATED
// FILE: Main.kt
hello
world
!!!
hello
world
!!!
hello2
world2
!!!
items:
  - 1
  - 2
  - 3
items:
  - 1
  - 2
  - 3