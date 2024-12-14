package com.bennyhuo.kotlin.trimindent.compiler

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrConstKind
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStringConcatenation
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid

class TrimIndentIrGenerator : IrGenerationExtension {


    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        moduleFragment.transformChildrenVoid(object : IrElementTransformerVoid() {
            override fun visitCall(irCall: IrCall): IrExpression {
                if (irCall.isTrimIndent()) {
                    val extensionReceiver = irCall.extensionReceiver!!
                    if (extensionReceiver is IrConst && extensionReceiver.kind == IrConstKind.String) {
                        val value = extensionReceiver.value as String
                        return super.visitExpression(extensionReceiver.copyWithNewValue(value.trimIndent()))
                    }

                    if (extensionReceiver is IrStringConcatenation) {
                        val elements = extensionReceiver.arguments.map { it.toStringElement() }
                        val irConstStringElements = elements.filterIsInstance<IrConstStringElement>()
                        // No string literals, done.
                        if (irConstStringElements.isEmpty()) {
                            return super.visitCall(irCall)
                        }
                        val minCommonIndent =
                            irConstStringElements.fold(ArrayList<String>()) { acc, element ->
                                if (element.values.isNotEmpty()) {
                                    acc += if (acc.isEmpty()) {
                                        element.values
                                    } else {
                                        // prefix${expression}postfix
                                        val prefix = acc.last()
                                        if (prefix.isBlank()) {
                                            // append whatever a non-blank character as a placeholder of the expression.
                                            acc[acc.lastIndex] = "$prefix!"
                                        }

                                        // the first value is not belong to a new line.
                                        // It follows the previous expression in the same line.
                                        element.values.subList(1, element.values.size)
                                    }
                                }
                                acc
                            }.minCommonIndent()

                        elements.first().safeAs<IrConstStringElement>()?.trimFirstEmptyLine()
                        elements.last().safeAs<IrConstStringElement>()?.trimLastEmptyLine()

                        val args = elements.fold(ArrayList<IrStringElement>()) { acc, element ->
                            acc += when(element) {
                                is IrConstStringElement -> {
                                    val start = if (acc.isEmpty()) 0 else 1
                                    for (i in start until element.values.size) {
                                        val content = element.values[i]
                                        element.values[i] = content.substring(minCommonIndent.coerceAtMost(content.length))
                                    }
                                    element
                                }
                                is IrExpressionElement -> {
                                    acc.lastOrNull()?.safeAs<IrConstStringElement>()?.let { last ->
                                        last.values.lastOrNull()?.takeIf { it.isBlank() }?.let { lastLine ->
                                            last.values[last.values.lastIndex] = ""
                                            element.reIndent(pluginContext, lastLine)
                                        }
                                    } ?: element
                                }
                            }
                            acc
                        }.map { element ->
                            when (element) {
                                is IrConstStringElement -> {
                                    element.irConst.copyWithNewValue(element.values.joinToString("\n"))
                                }
                                is IrExpressionElement -> {
                                    element.irExpression
                                }
                            }
                        }

                        return super.visitExpression(extensionReceiver.copyWithNewValues(args))
                    }
                }

                return super.visitCall(irCall)
            }
        })
    }
}
