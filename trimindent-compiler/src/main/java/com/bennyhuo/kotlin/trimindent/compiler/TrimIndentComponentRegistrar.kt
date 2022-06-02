package com.bennyhuo.kotlin.trimindent.compiler

import com.google.auto.service.AutoService
import com.intellij.mock.MockProject
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration

@AutoService(ComponentRegistrar::class)
class TrimIndentComponentRegistrar : ComponentRegistrar {

    override fun registerProjectComponents(
        project: MockProject,
        configuration: CompilerConfiguration
    ) {
        IrGenerationExtension.registerExtension(project, TrimIndentIrGenerator())
    }
}


