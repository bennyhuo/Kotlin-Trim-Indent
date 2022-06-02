package com.bennyhuo.kotlin.trimindent.compiler

import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrConstKind
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStringConcatenation
import org.jetbrains.kotlin.ir.expressions.impl.IrConstImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrStringConcatenationImpl
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Created by benny.
 */
fun IrConst<String>.copyWithNewValue(newValue: String) =
    IrConstImpl.string(startOffset, endOffset, type, newValue)

fun IrStringConcatenation.copyWithNewValues(
    arguments: Collection<IrExpression>
): IrStringConcatenationImpl {
    return IrStringConcatenationImpl(startOffset, endOffset, type, arguments)
}