package cn.staynoob.cu.defaultcu

import cn.staynoob.cu.DefaultCu
import cn.staynoob.cu.base.DataJpaTest
import cn.staynoob.cu.fixture.source.hostfirst1.BarEntity2
import cn.staynoob.cu.fixture.source.hostfirst1.BarSource2
import cn.staynoob.cu.fixture.source.hostfirst1.FooEntity2
import cn.staynoob.cu.fixture.source.hostfirst1.FooSource2
import cn.staynoob.cu.fixture.source.hostfirst2.BarEntity3
import cn.staynoob.cu.fixture.source.hostfirst2.BarSource3
import cn.staynoob.cu.fixture.source.hostfirst2.FooEntity3
import cn.staynoob.cu.fixture.source.hostfirst2.FooSource3
import cn.staynoob.cu.fixture.source.propertyfirst.BarEntity1
import cn.staynoob.cu.fixture.source.propertyfirst.BarSource1
import cn.staynoob.cu.fixture.source.propertyfirst.FooEntity1
import cn.staynoob.cu.fixture.source.propertyfirst.FooSource1
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@DisplayName("source property test")
class SourcePropertyTest : DataJpaTest() {

    @Autowired
    private lateinit var cu: DefaultCu

    @Nested
    inner class PropertyFirstTest {
        @Test
        @DisplayName("create test")
        fun test100() {
            val source = FooSource1("foo", BarSource1("bar"))
            val res = cu.create(source)
            val expected = FooEntity1("foo", BarEntity1("bar"))
            Assertions.assertThat(res).isEqualTo(expected)
        }

        @Test
        @DisplayName("update test")
        fun test200() {
            val foo = FooEntity1("foo", BarEntity1("foo"))
            val source = FooSource1("bar", BarSource1("bar"))
            cu.update(foo, source)
            val expected = FooEntity1("bar", BarEntity1("bar"))
            Assertions.assertThat(foo).isEqualTo(expected)
        }
    }

    @Nested
    inner class HostFirst1 {
        @Test
        @DisplayName("create test")
        fun test100() {
            val source = FooSource2("foo", BarSource2("bar"))
            val res = cu.create(source)

            val expected = FooEntity2("foo")
            val expectedBar = BarEntity2(expected, "bar")
            expected.bar = expectedBar

            Assertions.assertThat(res).isEqualTo(expected)
            assertThat(res.bar).isEqualTo(expectedBar)
        }

        @Test
        @DisplayName("update test")
        fun test200() {
            val foo = FooEntity2("foo")
            val bar = BarEntity2(foo, "foo")
            foo.bar = bar

            val source = FooSource2("bar", BarSource2("bar"))
            cu.update(foo, source)

            val expected = FooEntity2("bar")
            val expectedBar = BarEntity2(expected, "bar")
            expected.bar = expectedBar

            Assertions.assertThat(foo).isEqualTo(expected)
            assertThat(foo.bar).isEqualTo(expectedBar)
        }
    }

    @Nested
    inner class HostFirst2 {
        @Test
        @DisplayName("create test")
        fun test100() {
            val source = FooSource3("foo", listOf(BarSource3("bar")))
            val res = cu.create(source)

            val expected = FooEntity3("foo")
            val expectedBars = listOf(BarEntity3(expected, "bar"))
            expected.bars = expectedBars

            assertThat(res.foo).isEqualTo("foo")
            assertThat(res.bars).isEqualTo(expectedBars)
        }

        @Test
        @DisplayName("update test")
        fun test200() {
            val foo = FooEntity3("foo")
            val bar = BarEntity3(foo, "foo")
            foo.bars = listOf(bar)

            val source = FooSource3("bar", listOf(BarSource3("bar")))
            cu.update(foo, source)

            val expected = FooEntity3("bar")
            val expectedBars = listOf(BarEntity3(expected, "bar"))
            expected.bars = expectedBars

            Assertions.assertThat(foo).isEqualTo(expected)
            assertThat(foo.bars).isEqualTo(expectedBars)
        }
    }

}