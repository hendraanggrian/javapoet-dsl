package com.hendraanggrian.javapoet.collections

import com.hendraanggrian.javapoet.asTypeName
import com.squareup.javapoet.TypeName
import java.lang.reflect.Type
import kotlin.reflect.KClass

/** A [TypeNameList] is responsible for managing a set of type name instances. */
class TypeNameList internal constructor(actualList: MutableList<TypeName>) :
    MutableList<TypeName> by actualList {

    /** Add type name from [Class]. */
    fun add(type: Type): Boolean = add(type.asTypeName())

    /** Add type name from [KClass]. */
    fun add(type: KClass<*>): Boolean = add(type.asTypeName())

    /** Add type name from [T]. */
    inline fun <reified T> add(): Boolean = add(T::class.asTypeName())

    /** Convenient method to add type name with operator function. */
    operator fun plusAssign(type: Type) {
        add(type)
    }

    /** Convenient method to add type name with operator function. */
    operator fun plusAssign(type: KClass<*>) {
        add(type)
    }
}
