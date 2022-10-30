package com.bennyhuo.kotlin.trimindent.compiler

import com.bennyhuo.kotlin.compiletesting.extensions.module.IR_OUTPUT_TYPE_KOTLIN_LIKE
import com.bennyhuo.kotlin.compiletesting.extensions.module.KotlinModule
import com.bennyhuo.kotlin.compiletesting.extensions.module.checkResult
import com.bennyhuo.kotlin.compiletesting.extensions.source.FileBasedModuleInfoLoader
import org.junit.Test

/**
 * Created by benny at 2022/1/15 8:55 PM.
 */
class TrimIndentTest {

    @Test
    fun basic() {
        testBase("basic.kt")
    }


    private fun testBase(fileName: String) {
        val loader = FileBasedModuleInfoLoader("testData/$fileName")
        val sourceModuleInfos = loader.loadSourceModuleInfos()

        val modules = sourceModuleInfos.map {
            KotlinModule(it, componentRegistrars = listOf(TrimIndentComponentRegistrar()))
        }

        modules.checkResult(
            loader.loadExpectModuleInfos(),
            executeEntries = true,
            checkGeneratedIr = true,
            irOutputType = IR_OUTPUT_TYPE_KOTLIN_LIKE
        )
    }

}