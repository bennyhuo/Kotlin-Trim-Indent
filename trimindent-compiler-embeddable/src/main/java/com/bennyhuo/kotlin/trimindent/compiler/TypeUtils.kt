package com.bennyhuo.kotlin.trimindent.compiler

/**
 * Created by benny.
 */
inline fun <reified T : Any> Any?.safeAs(): T? = this as? T