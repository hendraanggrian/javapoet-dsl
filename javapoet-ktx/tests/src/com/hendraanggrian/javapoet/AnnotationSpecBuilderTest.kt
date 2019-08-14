package com.hendraanggrian.javapoet

import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.CodeBlock
import kotlin.test.Test
import kotlin.test.assertEquals

class AnnotationSpecBuilderTest {
    private val expected = AnnotationSpec.builder(Deprecated::class.java)
        .addMember("message", "Old stuff")
        .addMember(
            "code", CodeBlock.builder()
                .add("codeValue")
                .build()
        )
        .build()

    @Test
    fun simple() {
        assertEquals(expected, AnnotationSpecs.of<Deprecated> {
            members.add("message", "Old stuff")
            members.add("code") {
                append("codeValue")
            }
        })
    }

    @Test
    fun invocation() {
        assertEquals(expected, AnnotationSpecs.of<Deprecated> {
            members {
                add("message", "Old stuff")
                "code" {
                    append("codeValue")
                }
            }
        })
    }
}