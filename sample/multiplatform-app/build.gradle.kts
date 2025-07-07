plugins {
    alias(libs.plugins.kotlinMultiplatform)
    id("com.bennyhuo.kotlin.trimindent")
}

kotlin {
    jvm()
    macosArm64 {
        binaries.executable()
    }
}
