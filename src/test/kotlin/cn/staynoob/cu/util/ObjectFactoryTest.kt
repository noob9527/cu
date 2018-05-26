package cn.staynoob.cu.util

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.reflect.full.primaryConstructor

class ObjectFactoryTest {

    @Test
    @DisplayName("should create obj via specified constructor")
    fun test100() {
        open class Foo
        class Bar : Foo()

        val ctor = Bar::class.primaryConstructor
        val res = ObjectFactory.create(Foo::class, mapOf(), ctor)
        assertThat(res).isInstanceOf(Bar::class.java)
    }

    @Test
    @DisplayName("should create obj with matching parameter")
    fun test200() {
        data class Foo(
                val foo: String,
                val bar: String
        )

        val res = ObjectFactory.create(
                Foo::class,
                mapOf(
                        "bar" to "bar",
                        "foo" to "foo"
                ),
                Foo::class.primaryConstructor
        )
        assertThat(res).isEqualTo(Foo("foo", "bar"))
    }
}