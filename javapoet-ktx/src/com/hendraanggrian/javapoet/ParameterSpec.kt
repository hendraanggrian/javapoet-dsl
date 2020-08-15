package com.hendraanggrian.javapoet

import com.hendraanggrian.javapoet.collections.AnnotationSpecList
import com.hendraanggrian.javapoet.collections.AnnotationSpecListScope
import com.hendraanggrian.javapoet.collections.JavadocContainer
import com.hendraanggrian.javapoet.collections.JavadocContainerScope
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeName
import java.lang.reflect.Type
import javax.lang.model.element.Modifier
import javax.lang.model.element.VariableElement
import kotlin.reflect.KClass

/** Converts element to [ParameterSpec]. */
fun VariableElement.asParameterSpec(): ParameterSpec = ParameterSpec.get(this)

/** Builds new [ParameterSpec] from [TypeName]. */
fun parameterSpecOf(type: TypeName, name: String, vararg modifiers: Modifier): ParameterSpec =
    ParameterSpec.builder(type, name, *modifiers).build()

/** Builds new [ParameterSpec] from [Type]. */
fun parameterSpecOf(type: Type, name: String, vararg modifiers: Modifier): ParameterSpec =
    ParameterSpec.builder(type, name, *modifiers).build()

/** Builds new [ParameterSpec] from [KClass]. */
fun parameterSpecOf(type: KClass<*>, name: String, vararg modifiers: Modifier): ParameterSpec =
    ParameterSpec.builder(type.java, name, *modifiers).build()

/** Builds new [ParameterSpec] from [T]. */
inline fun <reified T> parameterSpecOf(name: String, vararg modifiers: Modifier): ParameterSpec =
    ParameterSpec.builder(T::class.java, name, *modifiers).build()

/**
 * Builds new [ParameterSpec] from [TypeName],
 * by populating newly created [ParameterSpecBuilder] using provided [builderAction] and then building it.
 */
inline fun buildParameterSpec(
    type: TypeName,
    name: String,
    vararg modifiers: Modifier,
    builderAction: ParameterSpecBuilder.() -> Unit
): ParameterSpec = ParameterSpec.builder(type, name, *modifiers).build(builderAction)

/**
 * Builds new [ParameterSpec] from [Type],
 * by populating newly created [ParameterSpecBuilder] using provided [builderAction] and then building it.
 */
inline fun buildParameterSpec(
    type: Type,
    name: String,
    vararg modifiers: Modifier,
    builderAction: ParameterSpecBuilder.() -> Unit
): ParameterSpec = ParameterSpec.builder(type, name, *modifiers).build(builderAction)

/**
 * Builds new [ParameterSpec] from [KClass],
 * by populating newly created [ParameterSpecBuilder] using provided [builderAction] and then building it.
 */
inline fun buildParameterSpec(
    type: KClass<*>,
    name: String,
    vararg modifiers: Modifier,
    builderAction: ParameterSpecBuilder.() -> Unit
): ParameterSpec = ParameterSpec.builder(type.java, name, *modifiers).build(builderAction)

/**
 * Builds new [ParameterSpec] from [T],
 * by populating newly created [ParameterSpecBuilder] using provided [builderAction] and then building it.
 */
inline fun <reified T> buildParameterSpec(
    name: String,
    vararg modifiers: Modifier,
    builderAction: ParameterSpecBuilder.() -> Unit
): ParameterSpec = ParameterSpec.builder(T::class.java, name, *modifiers).build(builderAction)

/** Modify existing [ParameterSpec.Builder] using provided [builderAction] and then building it. */
inline fun ParameterSpec.Builder.build(
    builderAction: ParameterSpecBuilder.() -> Unit
): ParameterSpec = ParameterSpecBuilder(this).apply(builderAction).build()

/** Wrapper of [ParameterSpec.Builder], providing DSL support as a replacement to Java builder. */
@SpecDslMarker
class ParameterSpecBuilder(private val nativeBuilder: ParameterSpec.Builder) {

    /** Modifiers of this parameter. */
    val modifiers: MutableList<Modifier> get() = nativeBuilder.modifiers

    /** Javadoc of this parameter. */
    val javadoc: JavadocContainer = object : JavadocContainer() {
        override fun append(format: String, vararg args: Any): Unit =
            format.formatWith(args) { s, array -> nativeBuilder.addJavadoc(s, *array) }

        override fun append(code: CodeBlock) {
            nativeBuilder.addJavadoc(code)
        }
    }

    /** Configures javadoc for this parameter. */
    inline fun javadoc(builderAction: JavadocContainerScope.() -> Unit): Unit =
        JavadocContainerScope(javadoc).builderAction()

    /** Annotations of this parameter. */
    val annotations: AnnotationSpecList = AnnotationSpecList(nativeBuilder.annotations)

    /** Configures annotations of this parameter. */
    inline fun annotations(builderAction: AnnotationSpecListScope.() -> Unit): Unit =
        AnnotationSpecListScope(annotations).builderAction()

    /** Add parameter modifiers. */
    fun addModifiers(vararg modifiers: Modifier) {
        nativeBuilder.addModifiers(*modifiers)
    }

    /** Returns native spec. */
    fun build(): ParameterSpec = nativeBuilder.build()
}
