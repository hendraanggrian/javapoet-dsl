package com.hendraanggrian.javapoet.dsl

import com.hendraanggrian.javapoet.JavapoetDslMarker
import com.hendraanggrian.javapoet.MethodSpecBuilder
import com.hendraanggrian.javapoet.buildConstructorMethod
import com.hendraanggrian.javapoet.buildMethod
import com.squareup.javapoet.MethodSpec

private interface MethodSpecAddable {

    /** Add method to this container. */
    fun add(spec: MethodSpec)
}

/** A [MethodSpecContainer] is responsible for managing a set of method instances. */
abstract class MethodSpecContainer internal constructor() : MethodSpecAddable {

    /** Add method from [name], returning the method added. */
    fun add(name: String): MethodSpec = buildMethod(name).also { add(it) }

    /** Add method from [name] with custom initialization [builderAction], returning the method added. */
    inline fun add(name: String, builderAction: MethodSpecBuilder.() -> Unit): MethodSpec =
        buildMethod(name, builderAction).also { add(it) }

    /** Add constructor method, returning the method added. */
    fun addConstructor(): MethodSpec = buildConstructorMethod().also { add(it) }

    /** Add constructor method with custom initialization [builderAction], returning the method added. */
    inline fun addConstructor(builderAction: MethodSpecBuilder.() -> Unit): MethodSpec =
        buildConstructorMethod(builderAction).also { add(it) }

    /** Convenient method to add method with operator function. */
    operator fun plusAssign(spec: MethodSpec) {
        add(spec)
    }

    /** Convenient method to add method with operator function. */
    operator fun plusAssign(name: String) {
        add(name)
    }

    /** Configure this container with DSL. */
    inline operator fun invoke(configuration: MethodSpecContainerScope.() -> Unit): Unit =
        MethodSpecContainerScope(this).configuration()
}

/** Receiver for the `methods` block providing an extended set of operators for the configuration. */
@JavapoetDslMarker
class MethodSpecContainerScope @PublishedApi internal constructor(container: MethodSpecContainer) :
    MethodSpecContainer(), MethodSpecAddable by container {

    /** Convenient method to add method with receiver type. */
    inline operator fun String.invoke(builderAction: MethodSpecBuilder.() -> Unit): MethodSpec =
        add(this, builderAction)
}
