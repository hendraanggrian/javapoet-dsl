package com.hendraanggrian.javapoet

import com.example.Annotation1
import com.example.Annotation2
import com.example.Annotation3
import com.example.Annotation4
import com.example.Annotation5
import com.example.Annotation6
import com.example.Annotation7
import com.google.common.truth.Truth.assertThat
import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.CodeBlock
import kotlin.test.Test
import kotlin.test.assertEquals

class AnnotationSpecHandlerTest {
    @Test
    fun annotation() {
        val field =
            buildFieldSpec(String::class.asTypeName(), "test") {
                annotation(Annotation1::class.asClassName())
                annotation(Annotation2::class.java)
                annotation(Annotation3::class)
                annotation<Annotation4>()
                annotation(Annotation5::class.asClassName()) { member("name5", "value5") }
                annotation(Annotation6::class.java) { member("name6", "value6") }
                annotation(Annotation7::class) { member("name7", "value7") }
            }
        assertThat(field.annotations).containsExactly(
            AnnotationSpec.builder(Annotation1::class.java).build(),
            AnnotationSpec.builder(Annotation2::class.java).build(),
            AnnotationSpec.builder(Annotation3::class.java).build(),
            AnnotationSpec.builder(Annotation4::class.java).build(),
            AnnotationSpec.builder(Annotation5::class.java).addMember("name5", "value5").build(),
            AnnotationSpec.builder(Annotation6::class.java).addMember("name6", "value6").build(),
            AnnotationSpec.builder(Annotation7::class.java).addMember("name7", "value7").build(),
        )
    }
}

class AnnotationSpecBuilderTest {
    @Test
    fun addMember() {
        assertEquals(
            buildAnnotationSpec(Annotation1::class.asClassName()) {
                member("member1", "value1")
                member("member2", codeBlockOf("value2"))
            },
            AnnotationSpec.builder(Annotation1::class.java)
                .addMember("member1", "value1")
                .addMember("member2", CodeBlock.of("value2"))
                .build(),
        )
    }
}