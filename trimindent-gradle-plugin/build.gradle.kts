plugins {
    id("java-gradle-plugin")
    kotlin("jvm")
    id("com.github.gmazzo.buildconfig")
}

dependencies {
    implementation(kotlin("gradle-plugin-api"))
    implementation(kotlin("stdlib"))
}

buildConfig {
    val compilerPluginProject = project(":trimindent-compiler")
    packageName("${compilerPluginProject.group}.trimindent")
    buildConfigField("String", "KOTLIN_PLUGIN_ID", "\"${property("KOTLIN_PLUGIN_ID")}\"")
    buildConfigField("String", "KOTLIN_PLUGIN_GROUP", "\"${compilerPluginProject.group}\"")
    buildConfigField("String", "KOTLIN_PLUGIN_NAME", "\"${compilerPluginProject.property("POM_ARTIFACT_ID")}\"")
    buildConfigField("String", "KOTLIN_PLUGIN_VERSION", "\"${compilerPluginProject.version}\"")
}

gradlePlugin {
    plugins {
        create("TrimIndentGradlePlugin") {
            id = project.properties["KOTLIN_PLUGIN_ID"] as String
            displayName = "Kotlin TrimIndent plugin"
            description = "Kotlin TrimIndent plugin"
            implementationClass = "com.bennyhuo.kotlin.trimindent.gradle.TrimIndentGradlePlugin"
        }
    }
}