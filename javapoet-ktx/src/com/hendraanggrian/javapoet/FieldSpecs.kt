package com.hendraanggrian.javapoet

import com.hendraanggrian.javapoet.dsl.AnnotationContainer
import com.hendraanggrian.javapoet.dsl.JavadocContainer
import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.TypeName
import javax.lang.model.element.Modifier
import kotlin.reflect.KClass

/** Builds a new [FieldSpec] from [type] supplying its [name] and [modifiers]. */
fun buildField(type: TypeName, name: String, vararg modifiers: Modifier): FieldSpec =
    FieldSpec.builder(type, name, *modifiers).build()

/**
 * Builds a new [FieldSpec] from [type] supplying its [name] and [modifiers],
 * by populating newly created [FieldSpecBuilder] using provided [builderAction] and then building it.
 */
inline fun buildField(
    type: TypeName,
    name: String,
    vararg modifiers: Modifier,
    builderAction: FieldSpecBuilder.() -> Unit
): FieldSpec = FieldSpecBuilder(FieldSpec.builder(type, name, *modifiers)).apply(builderAction).build()

/** Builds a new [FieldSpec] from [type] supplying its [name] and [modifiers]. */
fun buildField(type: Class<*>, name: String, vararg modifiers: Modifier): FieldSpec =
    FieldSpec.builder(type, name, *modifiers).build()

/** Builds a new [FieldSpec] from [type] supplying its [name] and [modifiers]. */
fun buildField(type: KClass<*>, name: String, vararg modifiers: Modifier): FieldSpec =
    buildField(type.java, name, *modifiers)

/** Builds a new [FieldSpec] from [T] supplying its [name] and [modifiers]. */
inline fun <reified T> buildField(name: String, vararg modifiers: Modifier): FieldSpec =
    buildField(T::class, name, *modifiers)

/**
 * Builds a new [FieldSpec] from [type] supplying its [name] and [modifiers],
 * by populating newly created [FieldSpecBuilder] using provided [builderAction] and then building it.
 */
inline fun buildField(
    type: Class<*>,
    name: String,
    vararg modifiers: Modifier,
    builderAction: FieldSpecBuilder.() -> Unit
): FieldSpec = FieldSpecBuilder(FieldSpec.builder(type, name, *modifiers)).apply(builderAction).build()

/**
 * Builds a new [FieldSpec] from [type] supplying its [name] and [modifiers],
 * by populating newly created [FieldSpecBuilder] using provided [builderAction] and then building it.
 */
inline fun buildField(
    type: KClass<*>,
    name: String,
    vararg modifiers: Modifier,
    builderAction: FieldSpecBuilder.() -> Unit
): FieldSpec = buildField(type.java, name, *modifiers, builderAction = builderAction)

/**
 * Builds a new [FieldSpec] from [T] supplying its [name] and [modifiers],
 * by populating newly created [FieldSpecBuilder] using provided [builderAction] and then building it.
 */
inline fun <reified T> buildField(
    name: String,
    vararg modifiers: Modifier,
    builderAction: FieldSpecBuilder.() -> Unit
): FieldSpec = buildField(T::class, name, *modifiers, builderAction = builderAction)

/** Wrapper of [FieldSpec.Builder], providing DSL support as a replacement to Java builder. */
@JavapoetDslMarker
class FieldSpecBuilder @PublishedApi internal constructor(private val nativeBuilder: FieldSpec.Builder) {

    /** Collection of javadoc, may be configured with Kotlin DSL. */
    val javadoc: JavadocContainer = object : JavadocContainer() {
        override fun append(format: String, vararg args: Any): Unit =
            format.formatWith(args) { s, array -> nativeBuilder.addJavadoc(s, *array) }

        override fun append(block: CodeBlock): CodeBlock =
            block.also { nativeBuilder.addJavadoc(it) }
    }

    /** Collection of annotations, may be configured with Kotlin DSL. */
    val annotations: AnnotationContainer = object : AnnotationContainer() {
        override fun add(spec: AnnotationSpec): AnnotationSpec =
            spec.also { nativeBuilder.addAnnotation(it) }
    }

    /** Add field modifiers. */
    fun addModifiers(vararg modifiers: Modifier) {
        nativeBuilder.addModifiers(*modifiers)
    }

    /** Set field value like [String.format]. */
    fun initializer(format: String, vararg args: Any): Unit =
        format.formatWith(args) { s, array -> nativeBuilder.initializer(s, *array) }

    /** Set field value to simple string. */
    inline var initializer: String
        @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = noGetter()
        set(value) = initializer(value)

    /** Set field value to code. */
    fun initializer(block: CodeBlock): CodeBlock =
        block.also { nativeBuilder.initializer(it) }

    /** Set field value to code with custom initialization [builderAction]. */
    inline fun initializer(builderAction: CodeBlockBuilder.() -> Unit): CodeBlock =
        initializer(buildCode(builderAction))

    /** Returns native spec. */
    fun build(): FieldSpec =
        nativeBuilder.build()
}
