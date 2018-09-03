package cn.staynoob.cu.defaultcu

import cn.staynoob.cu.AbstractCuSource
import cn.staynoob.cu.CuSource
import cn.staynoob.cu.DefaultCu
import cn.staynoob.cu.annotation.Ignore
import cn.staynoob.cu.annotation.IgnoreNull
import cn.staynoob.cu.annotation.NestedCuSource
import cn.staynoob.cu.base.DataJpaTest
import cn.staynoob.cu.domain.IntIdReference
import cn.staynoob.cu.domain.Reference
import cn.staynoob.cu.resolver.ResourceResolver
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.reflect.KClass
import kotlin.reflect.full.cast

class ExtractArgumentsTest : DataJpaTest() {

    @Autowired
    private lateinit var cu: DefaultCu

    @Test
    @DisplayName("should ignore inaccessible properties")
    fun test100() {
        class Target
        class Source(
                private val foo: String = "foo"
        ) : AbstractCuSource<Target>()

        val res = cu.extractArguments(Source())
        Assertions.assertThat(res).isEmpty()
    }

    @Test
    @DisplayName("should ignore properties which has property @Ignore annotation")
    fun test200() {
        class Target
        class Source(
                @Ignore
                val foo: String = "foo"
        ) : AbstractCuSource<Target>() {
            @Ignore
            val bar: String
                get() = "bar"
        }

        val res = cu.extractArguments(Source())
        Assertions.assertThat(res).isEmpty()
    }


    @Test
    @DisplayName("should ignore properties which has class scope @IgnoreNull annotation")
    fun test210() {
        class Target
        @IgnoreNull
        class Source(
                val foo: String? = null
        ) : AbstractCuSource<Target>() {
            val bar: String? = null
        }

        val res = cu.extractArguments(Source())
        Assertions.assertThat(res).isEmpty()
    }

    @Test
    @DisplayName("should ignore properties which has property scope @IgnoreNull annotation")
    fun test220() {
        class Target
        class Source(
                @IgnoreNull
                val foo: String? = null
        ) : AbstractCuSource<Target>() {
            @IgnoreNull
            val bar: String? = null
        }

        val res = cu.extractArguments(Source())
        Assertions.assertThat(res).isEmpty()
    }

    @Test
    @DisplayName("should extract 'data' properties correctly")
    fun test300() {
        class Target
        class Source(
                val foo: String = "foo"
        ) : AbstractCuSource<Target>() {
            val bar: String = "bar"
        }

        val res = cu.extractArguments(Source())

        Assertions.assertThat(res).isEqualTo(mapOf(
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
        ) : AbstractCuSource<Target>() {
            val bar: IntIdReference = IntIdReference(0).apply { resolver = MockResolver::class }
        }

        val res = cu.extractArguments(Source(), mapOf(MockResolver::class to MockResolver()))

        Assertions.assertThat(res).isEqualTo(mapOf(
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
        ) : AbstractCuSource<Target>()

        val res = cu.extractArguments(Source(), mapOf(MockResolver::class to MockResolver()))

        Assertions.assertThat(res).isEqualTo(mapOf(
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
                @NestedCuSource
                val foo: FooSource = FooSource()
        ) : AbstractCuSource<Target>()

        val res = cu.extractArguments(Source())

        Assertions.assertThat(res).isEqualTo(mapOf(
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
                @NestedCuSource
                val fooList: List<FooSource> = listOf(FooSource())
        ) : AbstractCuSource<Target>()

        val res = cu.extractArguments(Source())

        Assertions.assertThat(res).isEqualTo(mapOf(
                "fooList" to listOf(Foo())
        ))
    }
}