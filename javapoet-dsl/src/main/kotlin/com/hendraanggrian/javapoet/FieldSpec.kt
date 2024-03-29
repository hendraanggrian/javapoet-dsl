@file:OptIn(ExperimentalContracts::class)

package com.hendraanggrian.javapoet

import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.TypeName
import javax.lang.model.element.Modifier
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.reflect.KClass

public fun TypeName.asFieldSpec(name: String, vararg modifiers: Modifier): FieldSpec =
    FieldSpec.builder(this, name, *modifiers).build()

/**
 * Creates new [FieldSpec] by populating newly created [FieldSpecBuilder] using provided
 * [configuration].
 */
public inline fun buildFieldSpec(
    type: TypeName,
    name: String,
    vararg modifiers: Modifier,
    configuration: FieldSpecBuilder.() -> Unit,
): FieldSpec {
    contract { callsInPlace(configuration, InvocationKind.EXACTLY_ONCE) }
    return FieldSpecBuilder(FieldSpec.builder(type, name, *modifiers)).apply(configuration).build()
}

/**
 * Inserts new [FieldSpec] by populating newly created [FieldSpecBuilder] using provided
 * [configuration].
 */
public inline fun FieldSpecHandler.field(
    type: TypeName,
    name: String,
    vararg modifiers: Modifier,
    configuration: FieldSpecBuilder.() -> Unit,
): FieldSpec {
    contract { callsInPlace(configuration, InvocationKind.EXACTLY_ONCE) }
    return buildFieldSpec(type, name, *modifiers, configuration = configuration).also(::field)
}

/**
 * Inserts new [FieldSpec] by populating newly created [FieldSpecBuilder] using provided
 * [configuration].
 */
public inline fun FieldSpecHandler.field(
    type: Class<*>,
    name: String,
    vararg modifiers: Modifier,
    configuration: FieldSpecBuilder.() -> Unit,
): FieldSpec {
    contract { callsInPlace(configuration, InvocationKind.EXACTLY_ONCE) }
    return buildFieldSpec(type.name2, name, *modifiers, configuration = configuration)
        .also(::field)
}

/**
 * Inserts new [FieldSpec] by populating newly created [FieldSpecBuilder] using provided
 * [configuration].
 */
public inline fun FieldSpecHandler.field(
    type: KClass<*>,
    name: String,
    vararg modifiers: Modifier,
    configuration: FieldSpecBuilder.() -> Unit,
): FieldSpec {
    contract { callsInPlace(configuration, InvocationKind.EXACTLY_ONCE) }
    return buildFieldSpec(type.name, name, *modifiers, configuration = configuration)
        .also(::field)
}

/**
 * Property delegate for inserting new [FieldSpec] by populating newly created [FieldSpecBuilder]
 * using provided [configuration].
 */
public fun FieldSpecHandler.fielding(
    type: TypeName,
    vararg modifiers: Modifier,
    configuration: FieldSpecBuilder.() -> Unit,
): SpecDelegateProvider<FieldSpec> {
    contract { callsInPlace(configuration, InvocationKind.EXACTLY_ONCE) }
    return SpecDelegateProvider {
        buildFieldSpec(type, it, *modifiers, configuration = configuration).also(::field)
    }
}

/**
 * Property delegate for inserting new [FieldSpec] by populating newly created [FieldSpecBuilder]
 * using provided [configuration].
 */
public fun FieldSpecHandler.fielding(
    type: Class<*>,
    vararg modifiers: Modifier,
    configuration: FieldSpecBuilder.() -> Unit,
): SpecDelegateProvider<FieldSpec> {
    contract { callsInPlace(configuration, InvocationKind.EXACTLY_ONCE) }
    return SpecDelegateProvider {
        buildFieldSpec(type.name2, it, *modifiers, configuration = configuration)
            .also(::field)
    }
}

/**
 * Property delegate for inserting new [FieldSpec] by populating newly created [FieldSpecBuilder]
 * using provided [configuration].
 */
public fun FieldSpecHandler.fielding(
    type: KClass<*>,
    vararg modifiers: Modifier,
    configuration: FieldSpecBuilder.() -> Unit,
): SpecDelegateProvider<FieldSpec> {
    contract { callsInPlace(configuration, InvocationKind.EXACTLY_ONCE) }
    return SpecDelegateProvider {
        buildFieldSpec(type.name, it, *modifiers, configuration = configuration)
            .also(::field)
    }
}

/** Convenient method to insert [FieldSpec] using reified type. */
public inline fun <reified T> FieldSpecHandler.field(
    name: String,
    vararg modifiers: Modifier,
): FieldSpec = T::class.name.asFieldSpec(name, *modifiers).also(::field)

/** Invokes DSL to configure [FieldSpec] collection. */
public fun FieldSpecHandler.fields(configuration: FieldSpecHandlerScope.() -> Unit) {
    contract { callsInPlace(configuration, InvocationKind.EXACTLY_ONCE) }
    FieldSpecHandlerScope.of(this).configuration()
}

/** Responsible for managing a set of [FieldSpec] instances. */
public interface FieldSpecHandler {
    public fun field(field: FieldSpec)

    public fun field(type: TypeName, name: String, vararg modifiers: Modifier): FieldSpec =
        type.asFieldSpec(name, *modifiers).also(::field)

    public fun field(type: Class<*>, name: String, vararg modifiers: Modifier): FieldSpec =
        type.name2.asFieldSpec(name, *modifiers).also(::field)

    public fun field(type: KClass<*>, name: String, vararg modifiers: Modifier): FieldSpec =
        type.name.asFieldSpec(name, *modifiers).also(::field)

    public fun fielding(
        type: TypeName,
        vararg modifiers: Modifier,
    ): SpecDelegateProvider<FieldSpec> =
        SpecDelegateProvider { type.asFieldSpec(it, *modifiers).also(::field) }

    public fun fielding(
        type: Class<*>,
        vararg modifiers: Modifier,
    ): SpecDelegateProvider<FieldSpec> =
        SpecDelegateProvider { type.name2.asFieldSpec(it, *modifiers).also(::field) }

    public fun fielding(
        type: KClass<*>,
        vararg modifiers: Modifier,
    ): SpecDelegateProvider<FieldSpec> =
        SpecDelegateProvider { type.name.asFieldSpec(it, *modifiers).also(::field) }
}

/** Receiver for the `fields` block providing an extended set of operators for the configuration. */
@JavapoetDsl
public open class FieldSpecHandlerScope private constructor(
    handler: FieldSpecHandler,
) : FieldSpecHandler by handler {
    public companion object {
        public fun of(handler: FieldSpecHandler): FieldSpecHandlerScope =
            FieldSpecHandlerScope(handler)
    }

    /** @see field */
    public operator fun String.invoke(
        type: TypeName,
        vararg modifiers: Modifier,
        configuration: FieldSpecBuilder.() -> Unit,
    ): FieldSpec =
        buildFieldSpec(type, this, *modifiers, configuration = configuration).also(::field)

    /** @see field */
    public operator fun String.invoke(
        type: Class<*>,
        vararg modifiers: Modifier,
        configuration: FieldSpecBuilder.() -> Unit,
    ): FieldSpec =
        buildFieldSpec(type.name2, this, *modifiers, configuration = configuration).also(::field)

    /** @see field */
    public operator fun String.invoke(
        type: KClass<*>,
        vararg modifiers: Modifier,
        configuration: FieldSpecBuilder.() -> Unit,
    ): FieldSpec =
        buildFieldSpec(type.name, this, *modifiers, configuration = configuration).also(::field)
}

/** Wrapper of [FieldSpec.Builder], providing DSL support as a replacement to Java builder. */
@JavapoetDsl
public class FieldSpecBuilder(
    private val nativeBuilder: FieldSpec.Builder,
) : AnnotationSpecHandler {
    public val annotations: MutableList<AnnotationSpec> get() = nativeBuilder.annotations
    public val modifiers: MutableList<Modifier> get() = nativeBuilder.modifiers

    public fun javadoc(format: String, vararg args: Any): Unit =
        format.internalFormat(args) { format2, args2 ->
            nativeBuilder.addJavadoc(format2, *args2)
        }

    public fun javadoc(block: CodeBlock) {
        nativeBuilder.addJavadoc(block)
    }

    public override fun annotation(annotation: AnnotationSpec) {
        nativeBuilder.addAnnotation(annotation)
    }

    public fun modifiers(vararg modifiers: Modifier) {
        nativeBuilder.addModifiers(*modifiers)
    }

    public fun initializer(format: String, vararg args: Any) {
        initializer = codeBlockOf(format, *args)
    }

    public var initializer: CodeBlock
        @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR)
        get() = noGetter()
        set(value) {
            nativeBuilder.initializer(value)
        }

    public fun build(): FieldSpec = nativeBuilder.build()
}
