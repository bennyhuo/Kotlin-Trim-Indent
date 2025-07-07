// SOURCE
// FILE: Main.kt [MainKt#main]
fun main() {
    val list = listOf(1, 2, 3)
    val list2 = listOf("a", "b", "c")
    println("""
    A:
        ${list.joinToString("\n") {
            """
            - $it 
                ${list2.joinToString("\n") { "* $it" }}
            """.trimIndent()
        }}
    B: 
        ${
        list.joinToString("\n") {
            """
            - $it 
                ${list2.joinToString("\n") { "* $it" }}
        """.trimIndent()
        }
        }
    """.trimIndent())
}
// EXPECT
// FILE: MainKt.main.stdout
A:
    - 1
        * a
        * b
        * c
    - 2
        * a
        * b
        * c
    - 3
        * a
        * b
        * c
B:
    - 1
        * a
        * b
        * c
    - 2
        * a
        * b
        * c
    - 3
        * a
        * b
        * c
// FILE: Main.kt.ir
fun main() {
    val list = listOf(1, 2, 3)
    val list2 = listOf("a", "b", "c")
    println("A:\n${list.joinToString(
        separator = "\n"
    ) { it: Int ->
        "- $it \n${list2.joinToString(
            separator = "\n"
        ) { it: String ->
            "* $it"
        }
        .prependIndent("    ")}"
    }
    .prependIndent("    ")}\nB: \n${list.joinToString(
        separator = "\n"
    ) { it: Int ->
        "- $it \n${list2.joinToString(
            separator = "\n"
        ) { it: String ->
            "* $it"
        }
        .prependIndent("    ")}"
    }
    .prependIndent("    ")}")
}
