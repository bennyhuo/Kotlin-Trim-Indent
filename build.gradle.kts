buildscript {
    repositories {
        maven("https://mirrors.tencent.com/nexus/repository/maven-public")
    }
    dependencies {
        classpath("com.vanniktech:gradle-maven-publish-plugin:0.18.0")
    }
}

plugins {
    kotlin("jvm") apply false
    id("org.jetbrains.dokka") version "1.7.10" apply false
    id("com.github.gmazzo.buildconfig") version "2.1.0" apply false
    id("com.bennyhuo.kotlin.plugin.embeddable") version "1.7.10.0" apply false
    id("com.bennyhuo.kotlin.plugin.embeddable.test") version "1.7.10.0" apply false
}

subprojects {
    repositories {
        maven("https://mirrors.tencent.com/nexus/repository/maven-public")
    }

    if (!name.startsWith("sample") && parent?.name?.startsWith("sample") != true) {
        group = property("GROUP").toString()
        version = property("VERSION_NAME").toString()

        apply(plugin = "com.vanniktech.maven.publish")
    }

    pluginManager.withPlugin("java") {
        extensions.getByType<JavaPluginExtension>().sourceCompatibility = JavaVersion.VERSION_1_8
    }
}