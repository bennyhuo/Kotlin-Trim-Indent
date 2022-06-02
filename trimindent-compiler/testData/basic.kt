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

fun main() {
    println(s)
    println(s2)
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