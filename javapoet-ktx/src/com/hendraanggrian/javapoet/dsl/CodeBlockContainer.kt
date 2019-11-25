package com.hendraanggrian.javapoet.dsl

import com.hendraanggrian.javapoet.CodeBlockBlockBuilder
import com.hendraanggrian.javapoet.JavapoetDslMarker
import com.hendraanggrian.javapoet.buildCode
import com.squareup.javapoet.CodeBlock

private interface CodeBlockAppendable {

    /** Add code block to this container. */
    fun append(format: String, vararg args: Any)

    /** Add code block to this container. */
    fun append(code: CodeBlock)

    /** Add empty new line to this container. */
    fun appendln()

    /** Add code block with a new line to this container. */
    fun appendln(format: String, vararg args: Any)

    /** Add code block with a new line to this container. */
    fun appendln(code: CodeBlock)
}

abstract class CodeBlockCollection internal constructor() : CodeBlockAppendable {

    /** Add code block with custom initialization [builderAction], returning the block added. */
    inline fun append(builderAction: CodeBlockBlockBuilder.() -> Unit): CodeBlock =
        buildCode(builderAction).also { append(it) }

    override fun appendln() = appendln("")

    /** Add code block with custom initialization [builderAction] and a new line to this container, returning the block added. */
    inline fun appendln(builderAction: CodeBlockBlockBuilder.() -> Unit): CodeBlock =
        buildCode(builderAction).also { appendln(it) }

    /** Starts the control flow. */
    abstract fun beginFlow(flow: String, vararg args: Any)

    /** Continues the control flow. */
    abstract fun nextFlow(flow: String, vararg args: Any)

    /** Stops the control flow. */
    abstract fun endFlow()

    /** Stops the control flow. */
    abstract fun endFlow(flow: String, vararg args: Any)
}

/** A [JavadocContainer] is responsible for managing a set of code instances. */
abstract class JavadocContainer internal constructor() : CodeBlockAppendable {

    /** Add code block with custom initialization [builderAction], returning the block added. */
    inline fun append(builderAction: CodeBlockBlockBuilder.() -> Unit): CodeBlock =
        buildCode(builderAction).also { append(it) }

    override fun appendln(): Unit = append(SystemProperties.LINE_SEPARATOR)

    override fun appendln(format: String, vararg args: Any) {
        append(format, *args)
        appendln()
    }

    override fun appendln(code: CodeBlock) {
        append(code)
        appendln()
    }

    /** Add code block with custom initialization [builderAction] and a new line to this container, returning the block added. */
    inline fun appendln(builderAction: CodeBlockBlockBuilder.() -> Unit): CodeBlock =
        buildCode(builderAction).also { appendln(it) }

    /** Convenient method to add code block with operator function. */
    operator fun plusAssign(value: String) {
        append(value)
    }

    /** Convenient method to add code block with operator function. */
    operator fun plusAssign(code: CodeBlock) {
        append(code)
    }

    /** Configure this container with DSL. */
    inline operator fun invoke(configuration: JavadocContainerScope.() -> Unit): Unit =
        JavadocContainerScope(this).configuration()

    /**
     * @see kotlin.text.SystemProperties
     */
    private object SystemProperties {
        /** Line separator for current system. */
        @JvmField
        val LINE_SEPARATOR =
            checkNotNull(System.getProperty("line.separator")) { "Unable to obtain separator character." }
    }
}

/** Receiver for the `javadoc` block providing an extended set of operators for the configuration. */
@JavapoetDslMarker
class JavadocContainerScope @PublishedApi internal constructor(private val container: JavadocContainer) :
    JavadocContainer(), CodeBlockAppendable by container {

    override fun appendln(code: CodeBlock): Unit = container.appendln(code)
    override fun appendln(): Unit = container.appendln()
    override fun appendln(format: String, vararg args: Any): Unit = container.appendln(format, *args)
}
