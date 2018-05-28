package cn.staynoob.cu

import cn.staynoob.cu.annotation.Ignore
import cn.staynoob.cu.annotation.IgnoreNull
import cn.staynoob.cu.domain.IntIdReference
import cn.staynoob.cu.domain.Reference
import cn.staynoob.cu.fixture.AbstractCuSource
import cn.staynoob.cu.fixture.data.DataEntity
import cn.staynoob.cu.fixture.data.DataSource
import cn.staynoob.cu.fixture.reference.DumbEntity
import cn.staynoob.cu.fixture.reference.ReferenceEntity
import cn.staynoob.cu.fixture.reference.ReferenceSource
import cn.staynoob.cu.fixture.source.BarEntity
import cn.staynoob.cu.fixture.source.BarSource
import cn.staynoob.cu.fixture.source.FooEntity
import cn.staynoob.cu.fixture.source.FooSource
import cn.staynoob.cu.resolver.ResourceResolver
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.reflect.KClass
import kotlin.reflect.full.cast

class DefaultCuTest : DataJpaTest() {

    @Autowired
    private lateinit var cu: DefaultCu

    @Nested
    @DisplayName("data property test")
    inner class DataPropertyTest : DataJpaTest() {
        @Test
        @DisplayName("create test")
        fun test100() {
            val source = DataSource("foo")
            val res = cu.create(source)
            assertThat(res).isEqualTo(DataEntity("foo"))
        }

        @Test
        @DisplayName("update test")
        fun test200() {
            val foo = DataEntity("foo")
            val source = DataSource("bar")
            cu.update(foo, source)
            assertThat(foo).isEqualTo(DataEntity("bar"))
        }
    }

    @Nested
    @DisplayName("reference property test")
    inner class ReferencePropertyTest : DataJpaTest() {

        private val dumb1: DumbEntity = DumbEntity("dumb1")
        private val dumb2: DumbEntity = DumbEntity("dumb2")

        @BeforeEach
        fun setUp() {
            entityManager.persist(dumb1)
            entityManager.persist(dumb2)
            entityManager.flush()
        }

        @Test
        @DisplayName("create test")
        fun test100() {
            val source = ReferenceSource(IntIdReference(dumb1.id))
            val res = cu.create(source)
            assertThat(res)
                    .isEqualTo(ReferenceEntity(dumb1))
        }

        @Test
        @DisplayName("update test")
        fun test200() {
            val foo = ReferenceEntity(dumb1)
            val source = ReferenceSource(IntIdReference(dumb2.id))
            cu.update(foo, source)
            assertThat(foo)
                    .isEqualTo(ReferenceEntity(dumb2))
        }
    }

    @Nested
    inner class SourcePropertyTest : DataJpaTest() {
        @Test
        @DisplayName("create test")
        fun test100() {
            val source = FooSource("foo", BarSource("bar"))
            val res = cu.create(source)
            val expected = FooEntity("foo", BarEntity("bar"))
            assertThat(res).isEqualTo(expected)
        }

        @Test
        @DisplayName("update test")
        fun test200() {
            val foo = FooEntity("foo", BarEntity("foo"))
            val source = FooSource("bar", BarSource("bar"))
            cu.update(foo, source)
            val expected = FooEntity("bar", BarEntity("bar"))
            assertThat(foo).isEqualTo(expected)
        }
    }

    @Nested
    inner class ExtractArgumentsTest : DataJpaTest() {
        @Test
        @DisplayName("should ignore inaccessible properties")
        fun test100() {
            class Target
            class Source(
                    private val foo: String = "foo"
            ) : AbstractCuSource<Target>(Target::class)

            val res = cu.extractArguments(Source())
            assertThat(res).isEmpty()
        }

        @Test
        @DisplayName("should ignore properties which has property @Ignore annotation")
        fun test200() {
            class Target
            class Source(
                    @Ignore
                    val foo: String = "foo"
            ) : AbstractCuSource<Target>(Target::class) {
                @Ignore
                val bar: String
                    get() = "bar"
            }

            val res = cu.extractArguments(Source())
            assertThat(res).isEmpty()
        }


        @Test
        @DisplayName("should ignore properties which has class scope @IgnoreNull annotation")
        fun test210() {
            class Target
            @IgnoreNull
            class Source(
                    val foo: String? = null
            ) : AbstractCuSource<Target>(Target::class) {
                val bar: String? = null
            }

            val res = cu.extractArguments(Source())
            assertThat(res).isEmpty()
        }

        @Test
        @DisplayName("should ignore properties which has property scope @IgnoreNull annotation")
        fun test220() {
            class Target
            class Source(
                    @IgnoreNull
                    val foo: String? = null
            ) : AbstractCuSource<Target>(Target::class) {
                @IgnoreNull
                val bar: String? = null
            }

            val res = cu.extractArguments(Source())
            assertThat(res).isEmpty()
        }

        @Test
        @DisplayName("should extract 'data' properties correctly")
        fun test300() {
            class Target
            class Source(
                    val foo: String = "foo"
            ) : AbstractCuSource<Target>(Target::class) {
                val bar: String = "bar"
            }

            val res = cu.extractArguments(Source())

            assertThat(res).isEqualTo(mapOf(
                    "foo" to "foo",
                    "bar" to "bar"
            ))
        }


        @Test
        @DisplayName("should extract 'reference' properties correctly")
        fun test400() {
            data class Foo(val foo: String = "foo")
            data class Bar(val bar: String = "bar")
            data class Target(val foo: Foo, val bar: Bar)

            class MockResolver : ResourceResolver {
                override fun <T : Any> resolve(reference: Reference, targetClass: KClass<T>): T {
                    return when (targetClass) {
                        Foo::class -> targetClass.cast(Foo())
                        Bar::class -> targetClass.cast(Bar())
                        else -> throw Exception()
                    }
                }
            }

            class Source(
                    val foo: IntIdReference = IntIdReference(0).apply { resolver = MockResolver::class }
            ) : AbstractCuSource<Target>(Target::class) {
                val bar: IntIdReference = IntIdReference(0).apply { resolver = MockResolver::class }
            }

            val res = cu.extractArguments(Source(), mapOf(MockResolver::class to MockResolver()))

            assertThat(res).isEqualTo(mapOf(
                    "foo" to Foo(),
                    "bar" to Bar()
            ))
        }

        @Test
        @DisplayName("should extract collection of 'reference' properties correctly")
        fun test410() {
            data class Foo(val id: Int)
            data class Target(
                    val refs: List<Foo>
            )

            class MockResolver : ResourceResolver {
                override fun <T : Any> resolve(reference: Reference, targetClass: KClass<T>): T {
                    val id = (reference as IntIdReference).id!!
                    return targetClass.cast(Foo(id))
                }
            }

            class Source(
                    val refs: List<Reference> = listOf(
                            IntIdReference(1).apply { resolver = MockResolver::class },
                            IntIdReference(2).apply { resolver = MockResolver::class }
                    )
            ) : AbstractCuSource<Target>(Target::class)

            val res = cu.extractArguments(Source(), mapOf(MockResolver::class to MockResolver()))

            assertThat(res).isEqualTo(mapOf(
                    "refs" to listOf(Foo(1), Foo(2))
            ))
        }

        @Test
        @DisplayName("should extract 'CuSource' properties correctly")
        fun test500() {
            data class Foo(val foo: String = "foo")
            data class FooSource(val foo: String = "foo") : CuSource<Foo> {
                override val targetClass: KClass<Foo>
                    get() = Foo::class
            }

            data class Target(val foo: Foo)

            class Source(
                    val foo: FooSource = FooSource()
            ) : AbstractCuSource<Target>(Target::class)

            val res = cu.extractArguments(Source())

            assertThat(res).isEqualTo(mapOf(
                    "foo" to Foo()
            ))
        }

        @Test
        @DisplayName("should extract collection of 'CuSource' properties correctly")
        fun test510() {
            data class Foo(val foo: String = "foo")
            data class FooSource(val foo: String = "foo") : CuSource<Foo> {
                override val targetClass: KClass<Foo>
                    get() = Foo::class
            }

            data class Target(val fooList: List<Foo>)

            class Source(
                    val fooList: List<FooSource> = listOf(FooSource())
            ) : AbstractCuSource<Target>(Target::class)

            val res = cu.extractArguments(Source())

            assertThat(res).isEqualTo(mapOf(
                    "fooList" to listOf(Foo())
            ))
        }
    }
}