pluginManagement {
    repositories {
        maven("https://mirrors.tencent.com/nexus/repository/maven-public")
        gradlePluginPortal()
    }
}

include("trimindent-compiler")
include("trimindent-compiler-embeddable")
include("trimindent-gradle-plugin")