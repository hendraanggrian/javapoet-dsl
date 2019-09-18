package com.hendraanggrian.javapoet

import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.FieldSpec
import kotlin.test.Test
import kotlin.test.assertEquals

class FieldSpecBuilderTest {
    private val expected = FieldSpec.builder(String::class.java, "name")
        .addJavadoc("firstJavadoc")
        .addJavadoc(
            CodeBlock.builder()
                .add("secondJavadoc")
                .build()
        )
        .addAnnotation(AnnotationSpec.builder(Deprecated::class.java).build())
        .addModifiers(public, final)
        .initializer("value")
        .build()

    @Test
    fun simple() {
        assertEquals(expected, buildField<String>("name") {
            javadoc.append("firstJavadoc")
            javadoc.append {
                append("secondJavadoc")
            }
            annotations.add<Deprecated>()
            addModifiers(public, final)
            initializer = "value"
        })
    }

    @Test
    fun invocation() {
        assertEquals(expected, buildField<String>("name") {
            javadoc {
                append("firstJavadoc")
                append {
                    append("secondJavadoc")
                }
            }
            annotations {
                add<Deprecated>()
            }
            addModifiers(public, final)
            initializer = "value"
        })
    }
}