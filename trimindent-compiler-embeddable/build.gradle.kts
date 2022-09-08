plugins {
    java
    id("com.bennyhuo.kotlin.plugin.embeddable")
}

dependencies {
    embedded(project(":trimindent-compiler"))
}