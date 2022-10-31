// SOURCE
// FILE: Main.kt [MainKt#main]
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

val s4 = """
    private $s2 // End of the method.
""".trimIndent()

fun main() {
    println(s2)
    println(s3)
    println(s4)
}
// EXPECT
// FILE: Main.kt.stdout
def test(a) {
    if(a > 1) {
        return true
    }
}
class Test {
    def test(a) {
        if(a > 1) {
            return true
        }
    }
}
private def test(a) {
    if(a > 1) {
        return true
    }
} // End of the method.
// FILE: Main.kt.ir
val s1: String = """if(a > 1) {
    return true
}"""
val s2: String = "def test(a) {\n${s1.prependIndent("    ")}\n}"
val s3: String = "class Test {\n${s2.prependIndent("    ")}\n}"
val s4: String = "private ${s2} // End of the method."
fun main() {
    println(s2)
    println(s3)
    println(s4)
}