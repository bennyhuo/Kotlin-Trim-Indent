package com.bennyhuo.kotlin.trimindent.compiler

import com.bennyhuo.kotlin.compiletesting.extensions.module.KotlinModule
import com.bennyhuo.kotlin.compiletesting.extensions.module.checkResult
import com.bennyhuo.kotlin.compiletesting.extensions.module.compileAll
import com.bennyhuo.kotlin.compiletesting.extensions.module.resolveAllDependencies
import com.bennyhuo.kotlin.compiletesting.extensions.result.ResultCollector
import com.bennyhuo.kotlin.compiletesting.extensions.source.SingleFileModuleInfoLoader
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
        val loader = SingleFileModuleInfoLoader("testData/$fileName")
        val sourceModuleInfos = loader.loadSourceModuleInfos()

        val modules = sourceModuleInfos.map {
            KotlinModule(it, componentRegistrars = listOf(TrimIndentComponentRegistrar()))
        }

        modules.resolveAllDependencies()
        modules.compileAll()
        modules.checkResult(
            loader.loadExpectModuleInfos(),
            executeEntries = true
        )
    }

}