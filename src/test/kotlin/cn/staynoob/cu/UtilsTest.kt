package cn.staynoob.cu

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.reflect.full.primaryConstructor

class UtilsTest {

    @Nested
    inner class CreateInstanceTest {

        @Test
        @DisplayName("should create instance with matching parameter")
        fun test200() {
            data class Foo(
                    val foo: String,
                    val bar: String
            )

            val res = Utils.createInstance(
                    Foo::class.primaryConstructor!!,
                    mapOf(
                            "bar" to "bar",
                            "foo" to "foo"
                    )
            )
            assertThat(res).isEqualTo(Foo("foo", "bar"))
        }

        @Nested
        inner class AssignPropertiesTest {
            @Test
            @DisplayName("should assign matched property")
            fun test100() {
                class Foo {
                    var foo: String? = null
                }

                val instance = Foo()
                Utils.assignProperties(instance, mapOf("foo" to "foo"))
                assertThat(instance.foo).isEqualTo("foo")
            }

            @Test
            @DisplayName("should ignore not compatible property")
            fun test200() {
                class Foo {
                    var foo: String? = null
                }

                val instance = Foo()
                Utils.assignProperties(instance, mapOf("foo" to 0))
                assertThat(instance.foo).isNull()
            }

            @Test
            @DisplayName("should ignore inaccessable property")
            fun test300() {
                class Foo {
                    var foo: String? = null
                        private set
                }

                val instance = Foo()
                Utils.assignProperties(instance, mapOf("foo" to "foo"))
                assertThat(instance.foo).isNull()
            }
        }

        @Nested
        inner class ResolvePropertyClassTest {
            @Test
            @DisplayName("should resolve target class correctly")
            fun test100() {
                class Foo(
                        val test1: String,
                        val test2: String?
                )
                assertThat(Utils.resolveTargetClass(Foo::class, "test1"))
                        .isEqualTo(String::class)
                assertThat(Utils.resolveTargetClass(Foo::class, "test2"))
                        .isEqualTo(String::class)
            }

            @Test
            @DisplayName("should resolve collection element class correctly")
            fun test200() {
                class Bar
                class Foo(
                        val test1: List<String>,
                        val test2: List<Bar>
                )
                assertThat(Utils.resolveTargetClass(Foo::class, "test1"))
                        .isEqualTo(String::class)
                assertThat(Utils.resolveTargetClass(Foo::class, "test2"))
                        .isEqualTo(Bar::class)
            }
        }

    }
}