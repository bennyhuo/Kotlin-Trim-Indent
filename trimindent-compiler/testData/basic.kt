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

// EXPECT
// FILE: Main.kt.stdout
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
// FILE: Main.kt.ir
val s: String = """hello
world
!!!"""
val s2: String = """${s}
hello2
world2
!!!"""
val string: String = """items:
  - 1
  - 2
  - 3"""
val items: List<Int> = listOf(1, 2, 3)
val string2: String = "items:\n${items.joinToString(
  separator = "\n"
) { it: Int ->
  "  - $it"
}
}"
fun main() {
  println(s)
  println(s2)
  println(string)
  println(string2)
}
