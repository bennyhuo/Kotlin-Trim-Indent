pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

include("trimindent-compiler")
include("trimindent-compiler-embeddable")
include("trimindent-gradle-plugin")

val local = file("composite_build.local")
if (local.exists()) {
    local.readLines().forEach {
        val f = file("../$it")
        if (f.exists()) {
            includeBuild(f)
        }
    }
}