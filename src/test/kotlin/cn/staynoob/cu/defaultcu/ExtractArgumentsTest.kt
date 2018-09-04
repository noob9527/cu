package cn.staynoob.cu.defaultcu

import cn.staynoob.cu.AbstractCuSource
import cn.staynoob.cu.DefaultCu
import cn.staynoob.cu.annotation.Ignore
import cn.staynoob.cu.annotation.IgnoreNull
import cn.staynoob.cu.base.DataJpaTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

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
                .mapKeys { it.key.name }

        Assertions.assertThat(res).isEqualTo(mapOf(
                "foo" to "foo",
                "bar" to "bar"
        ))
    }
}