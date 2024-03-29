package com.hendraanggrian.javapoet

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Property delegate accessor when configuring [com.hendraanggrian.javapoet.collections] with `by`
 * keyword.
 */
public class SpecDelegateProvider<T>(private val getSpec: (String) -> T) {
    public operator fun provideDelegate(
        thisRef: Any?,
        property: KProperty<*>,
    ): ReadOnlyProperty<Any?, T> {
        val spec = getSpec(property.name)
        return ReadOnlyProperty { _, _ -> spec }
    }
}
