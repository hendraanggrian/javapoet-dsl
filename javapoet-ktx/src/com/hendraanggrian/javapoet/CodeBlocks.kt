package com.hendraanggrian.javapoet

import com.hendraanggrian.javapoet.dsl.CodeBlockCollection
import com.squareup.javapoet.CodeBlock

/**
 * Converts string to [CodeBlock] using formatted [args].
 *
 * @see kotlin.text.format
 */
fun String.formatCode(vararg args: Any): CodeBlock = formatWith(args) { s, array -> CodeBlock.of(s, *array) }

/**
 * Builds a new [CodeBlock],
 * by populating newly created [CodeBlockBlockBuilder] using provided [builderAction] and then building it.
 */
inline fun buildCode(builderAction: CodeBlockBlockBuilder.() -> Unit): CodeBlock =
    CodeBlockBlockBuilder(CodeBlock.builder()).apply(builderAction).build()

/** Joins code blocks into a single [CodeBlock], each separated by [separator]. */
fun Iterable<CodeBlock>.join(separator: String): CodeBlock = CodeBlock.join(this, separator)

/** Wrapper of [CodeBlock.Builder], providing DSL support as a replacement to Java builder. */
@JavapoetDslMarker
class CodeBlockBlockBuilder @PublishedApi internal constructor(private val nativeBuilder: CodeBlock.Builder) :
    CodeBlockCollection() {

    /** Returns true if this builder contains no code. */
    fun isEmpty(): Boolean = nativeBuilder.isEmpty

    /** Returns true if this builder contains code. */
    fun isNotEmpty(): Boolean = !nativeBuilder.isEmpty

    /** Adds code using named arguments. */
    fun addNamed(format: String, arguments: Map<String, *>): Unit =
        format.formatWith(arguments) { s, map -> nativeBuilder.addNamed(s, map) }

    override fun append(format: String, vararg args: Any): Unit =
        format.formatWith(args) { s, array -> nativeBuilder.add(s, *array) }

    override fun beginFlow(flow: String, vararg args: Any): Unit =
        flow.formatWith(args) { s, array -> nativeBuilder.beginControlFlow(s, *array) }

    override fun nextFlow(flow: String, vararg args: Any): Unit =
        flow.formatWith(args) { s, array -> nativeBuilder.nextControlFlow(s, *array) }

    override fun endFlow() {
        nativeBuilder.endControlFlow()
    }

    override fun endFlow(flow: String, vararg args: Any): Unit =
        flow.formatWith(args) { s, array -> nativeBuilder.endControlFlow(s, *array) }

    override fun appendln(format: String, vararg args: Any): Unit =
        format.formatWith(args) { s, array -> nativeBuilder.addStatement(s, *array) }

    override fun appendln(code: CodeBlock) {
        nativeBuilder.addStatement(code)
    }

    override fun append(code: CodeBlock) {
        nativeBuilder.add(code)
    }

    /** Indent current code. */
    fun indent() {
        nativeBuilder.indent()
    }

    /** Unindent current code. */
    fun unindent() {
        nativeBuilder.unindent()
    }

    /** Returns native spec. */
    fun build(): CodeBlock = nativeBuilder.build()
}
