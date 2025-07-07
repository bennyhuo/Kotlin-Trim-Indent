// SOURCE
// FILE: Main.kt [MainKt#main]
val s = """
        
        hello
        
        """.trimIndent()

val s1 = """
        $s
        """.trimIndent()


val s2 = """
         hello
       $s
         world
         """.trimIndent()
fun main() {
    println(s)
    println(s1)
    println(s2)
}
// EXPECT
// FILE: MainKt.main.stdout

hello


hello

  hello

hello

  world
// FILE: Main.kt.ir
val s: String = "\nhello\n"
val s1: String = "${s}"
val s2: String = "  hello\n${s}\n  world"
fun main() {
    println(s)
    println(s1)
    println(s2)
}
