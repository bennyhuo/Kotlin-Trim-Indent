package com.bennyhuo.kotlin.trimindent.compiler

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrConstKind
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStringConcatenation
import org.jetbrains.kotlin.ir.expressions.impl.IrConstImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrStringConcatenationImpl
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.util.fqNameWhenAvailable
import org.jetbrains.kotlin.ir.util.getPackageFragment
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import org.jetbrains.kotlin.name.CallableId

/**
 * Created by benny.
 */
fun IrConst.copyWithNewValue(newValue: String) =
    IrConstImpl.string(startOffset, endOffset, type, newValue)

fun IrStringConcatenation.copyWithNewValues(
    arguments: Collection<IrExpression>
): IrStringConcatenationImpl {
    return IrStringConcatenationImpl(startOffset, endOffset, type, arguments)
}

internal fun IrCall.isTrimIndent(): Boolean {
    return symbol.owner.name == Name.identifier("trimIndent")
            && dispatchReceiver == null
            && extensionReceiver?.type?.classFqName?.asString() == "kotlin.String"
            && symbol.owner.getPackageFragment().packageFqName.asString() == "kotlin.text"
}

fun IrPluginContext.prependIndent(): IrFunction {
    return referenceFunctions(CallableId(FqName("kotlin.text"), Name.identifier("prependIndent")))
        .singleOrNull {
            it.owner.extensionReceiverParameter?.type?.classFqName?.asString() == "kotlin.String"
        }?.owner!!
}
