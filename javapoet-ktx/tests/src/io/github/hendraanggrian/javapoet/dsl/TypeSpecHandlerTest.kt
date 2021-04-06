package io.github.hendraanggrian.javapoet.dsl

import com.google.common.truth.Truth.assertThat
import io.github.hendraanggrian.javapoet.annotationTypeSpecOf
import io.github.hendraanggrian.javapoet.anonymousTypeSpecOf
import io.github.hendraanggrian.javapoet.buildEnumTypeSpec
import io.github.hendraanggrian.javapoet.classOf
import io.github.hendraanggrian.javapoet.classTypeSpecOf
import io.github.hendraanggrian.javapoet.interfaceTypeSpecOf
import kotlin.test.Test

class TypeSpecHandlerTest {
    private val list = TypeSpecHandler(mutableListOf())

    private inline fun container(configuration: TypeSpecHandlerScope.() -> Unit) =
        TypeSpecHandlerScope(list).configuration()

    @Test
    fun nativeSpec() {
        list += classTypeSpecOf("Class1")
        list += listOf(classTypeSpecOf("Class2"))
        assertThat(list).containsExactly(
            classTypeSpecOf("Class1"),
            classTypeSpecOf("Class2")
        )
    }

    @Test
    fun invocation() {
        val packageName = "io.github.hendraanggrian.javapoet.collections.TypeSpecListTest"
        container {
            `class`("Class1")
            `class`(packageName.classOf("MyType"))
        }
        assertThat(list).containsExactly(
            classTypeSpecOf("Class1"),
            classTypeSpecOf(packageName.classOf("MyType"))
        )
    }

    @Test
    fun others() {
        list.addClass("Class1")
        list.addInterface("Interface1")
        list.addEnum("Enum1") { addEnumConstant("A") }
        list.addAnonymous("Anonymous1")
        list.addAnnotation("Annotation1")
        assertThat(list).containsExactly(
            classTypeSpecOf("Class1"),
            interfaceTypeSpecOf("Interface1"),
            buildEnumTypeSpec("Enum1") { addEnumConstant("A") },
            anonymousTypeSpecOf("Anonymous1"),
            annotationTypeSpecOf("Annotation1")
        )
    }
}