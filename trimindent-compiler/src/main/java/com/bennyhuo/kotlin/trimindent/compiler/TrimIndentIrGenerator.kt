package com.bennyhuo.kotlin.trimindent.compiler

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid

class TrimIndentIrGenerator: IrGenerationExtension {

    private fun IrCall.isTrimIndent(): Boolean {
        return dispatchReceiver == null && extensionReceiver != null
                && symbol.owner.kotlinFqName.asString() == "kotlin.text.trimIndent"
    }

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        moduleFragment.transformChildrenVoid(object: IrElementTransformerVoid() {
            override fun visitCall(irCall: IrCall): IrExpression {
                if (irCall.isTrimIndent()) {
                    val extensionReceiver = irCall.extensionReceiver
                    if(extensionReceiver is IrConst<*> && extensionReceiver.kind == IrConstKind.String) {
                        extensionReceiver as IrConst<String>
                        return extensionReceiver.copyWithNewValue(extensionReceiver.value.trimIndent())
                    }

                    if (extensionReceiver is IrStringConcatenation) {
                        val elements = extensionReceiver.arguments.map { it.toStringElement() }
                        val minCommonIndent = elements.filterIsInstance<ConstStringElement>()
                            .flatMap { it.values }
                            .minCommonIndent()

                        val lastIndex = elements.lastIndex
                        val args = elements.mapIndexedNotNull { index, element ->
                            when (element) {
                                is ConstStringElement -> {
                                    val lastContentIndex = element.values.lastIndex
                                    val newValue = element.values.mapIndexedNotNull { contentIndex, content ->
                                        if (index == lastIndex && contentIndex == lastContentIndex && content.isBlank()) {
                                            null
                                        } else {
                                            content.substring(minCommonIndent.coerceAtMost(content.length))
                                        }
                                    }.joinToString("\n")

                                    if ((index == 0 || index == lastIndex) && newValue.isBlank()){
                                        null
                                    } else {
                                        element.irConst.copyWithNewValue(newValue)
                                    }
                                }
                                is UnknownElement -> {
                                    element.irExpression
                                }
                            }
                        }

                        return extensionReceiver.copyWithNewValues(args)
                    }
                }

                return super.visitCall(irCall)
            }
        })
    }
}