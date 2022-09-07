package com.bennyhuo.kotlin.trimindent.compiler

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid
import org.jetbrains.kotlin.utils.addToStdlib.safeAs

class TrimIndentIrGenerator : IrGenerationExtension {

    private fun IrCall.isTrimIndent(): Boolean {
        return dispatchReceiver == null && extensionReceiver != null
                && symbol.owner.kotlinFqName.asString() == "kotlin.text.trimIndent"
    }

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        moduleFragment.transformChildrenVoid(object : IrElementTransformerVoid() {
            override fun visitCall(irCall: IrCall): IrExpression {
                if (irCall.isTrimIndent()) {
                    val extensionReceiver = irCall.extensionReceiver!!
                    if (extensionReceiver is IrConst<*> && extensionReceiver.kind == IrConstKind.String) {
                        extensionReceiver as IrConst<String>
                        return extensionReceiver.copyWithNewValue(extensionReceiver.value.trimIndent())
                    }

                    if (extensionReceiver is IrStringConcatenation) {
                        val elements = extensionReceiver.arguments.map { it.toStringElement() }
                        val constStringElements = elements.filterIsInstance<ConstStringElement>()
                        // No string literals, done.
                        if (constStringElements.isEmpty()) {
                            return super.visitCall(irCall)
                        }
                        val minCommonIndent = constStringElements.flatMap { it.values }.minCommonIndent()

                        elements.first().safeAs<ConstStringElement>()?.trimFirstEmptyLine()
                        elements.last().safeAs<ConstStringElement>()?.trimLastEmptyLine()

                        val args = elements.map { element ->
                            when (element) {
                                is ConstStringElement -> {
                                    element.irConst.copyWithNewValue(
                                        element.values.joinToString("\n") { content ->
                                            content.substring(minCommonIndent.coerceAtMost(content.length))
                                        }
                                    )
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