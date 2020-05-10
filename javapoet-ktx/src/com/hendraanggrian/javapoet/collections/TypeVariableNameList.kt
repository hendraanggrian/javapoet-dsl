package com.hendraanggrian.javapoet.collections

import com.hendraanggrian.javapoet.typeVarBy
import com.hendraanggrian.javapoet.typeVarOf
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeVariableName
import java.lang.reflect.Type
import kotlin.reflect.KClass

/** A [TypeVariableNameList] is responsible for managing a set of type variable name instances. */
class TypeVariableNameList internal constructor(actualList: MutableList<TypeVariableName>) :
    MutableList<TypeVariableName> by actualList {

    /** Add a [TypeVariableName] without bounds. */
    fun add(name: String): Boolean = add(name.typeVarOf())

    /** Returns a [TypeVariableName] with [TypeName] bounds. */
    fun add(name: String, vararg bounds: TypeName): Boolean = add(name.typeVarBy(*bounds))

    /** Returns a [TypeVariableName] with [Type] bounds. */
    fun add(name: String, vararg bounds: Type): Boolean = add(name.typeVarBy(*bounds))

    /** Returns a [TypeVariableName] with [KClass] bounds. */
    fun add(name: String, vararg bounds: KClass<*>): Boolean = add(name.typeVarBy(*bounds))

    /** Convenient method to add type name with operator function. */
    operator fun plusAssign(type: String) {
        add(type)
    }
}
