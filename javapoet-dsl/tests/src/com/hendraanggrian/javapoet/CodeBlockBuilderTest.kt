package com.hendraanggrian.javapoet

import com.squareup.javapoet.CodeBlock
import kotlin.test.Test
import kotlin.test.assertEquals

class CodeBlockBuilderTest {

    @Test
    fun simple() {
        assertEquals(
            CodeBlock.of("Hello world"),
            CodeBlockBuilder.of { add("Hello world") }
        )
    }

    @Test
    fun advanced() {
        assertEquals(
            CodeBlock.builder()
                .addStatement("int total = 0")
                .beginControlFlow("for (int i = 0; i < 10; i++)")
                .addStatement("total += i")
                .endControlFlow()
                .build(),
            CodeBlockBuilder.of {
                statements.add("int total = 0")
                beginControlFlow("for (int i = 0; i < 10; i++)")
                statements.add("total += i")
                endControlFlow()
            }
        )
    }
}