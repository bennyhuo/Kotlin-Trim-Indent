package com.bennyhuo.kotlin.trimindent.compiler

import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrConstKind
import org.jetbrains.kotlin.ir.expressions.IrExpression

/**
 * Created by benny.
 */
fun List<String>.minCommonIndent(): Int {
    return filter(String::isNotBlank).minOfOrNull { string ->
        string.indexOfFirst {
            !it.isWhitespace()
        }.let {
            if (it == -1) string.length else it
        }
    } ?: 0
}

sealed interface IrStringElement

fun IrExpression.toStringElement(): IrStringElement {
    return if(this is IrConst<*> && this.kind == IrConstKind.String) {
        ConstStringElement(this as IrConst<String>)
    } else {
        UnknownElement(this)
    }
}

class UnknownElement(val irExpression: IrExpression): IrStringElement

class ConstStringElement(val irConst: IrConst<String>): IrStringElement {

    val values = irConst.value.split("\n").toMutableList()

    fun trimFirstEmptyLine() {
        if (values.firstOrNull()?.isBlank() == true) values.removeFirst()
    }

    fun trimLastEmptyLine() {
        if (values.lastOrNull()?.isBlank() == true) values.removeLast()
    }
}