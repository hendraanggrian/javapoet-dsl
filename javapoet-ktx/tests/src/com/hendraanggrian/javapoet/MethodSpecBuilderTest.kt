package com.hendraanggrian.javapoet

import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeName
import java.io.IOException
import javax.lang.model.element.Modifier
import kotlin.test.Test
import kotlin.test.assertEquals

class MethodSpecBuilderTest {
    private val getBuilder = { MethodSpec.methodBuilder("main") }
    private val expected = getBuilder()
        .addJavadoc("firstJavadoc")
        .addJavadoc(
            CodeBlock.builder()
                .add("secondJavadoc")
                .build()
        )
        .addAnnotation(AnnotationSpec.builder(Deprecated::class.java).build())
        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
        .returns(TypeName.VOID)
        .addParameter(ParameterSpec.builder(Array<String>::class.java, "param").build())
        .varargs(true)
        .addException(IOException::class.java)
        .addComment("Some comment")
        .addCode("doSomething()")
        .build()

    @Test
    fun simple() {
        assertEquals(expected, (getBuilder()) {
            javadoc.add("firstJavadoc")
            javadoc.add {
                append("secondJavadoc")
            }
            annotations.add<Deprecated>()
            addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            returns = TypeName.VOID
            parameters.add<Array<String>>("param")
            varargs = true
            addException<IOException>()
            addComment("Some comment")
            codes.append("doSomething()")
        })
    }

    @Test
    fun invocation() {
        assertEquals(expected, (getBuilder()) {
            javadoc {
                add("firstJavadoc")
                add {
                    append("secondJavadoc")
                }
            }
            annotations {
                add<Deprecated>()
            }
            addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            returns = TypeName.VOID
            parameters {
                add<Array<String>>("param")
            }
            varargs = true
            addException<IOException>()
            addComment("Some comment")
            codes {
                append("doSomething()")
            }
        })
    }
}