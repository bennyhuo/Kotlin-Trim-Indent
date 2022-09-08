pluginManagement {
    repositories {
        maven("https://mirrors.tencent.com/nexus/repository/maven-public")
        gradlePluginPortal()
    }
}

include("trimindent-compiler")
include("trimindent-compiler-embeddable")
include("trimindent-gradle-plugin")

listOf(
    "kotlin-compile-testing-extensions",
    "kotlin-compiler-plugin-embeddable-plugin"
).forEach {
    val projectFile = file("../$it")
    if (projectFile.exists()) {
        includeBuild(projectFile)
    }
}