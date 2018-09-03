package cn.staynoob.cu.defaultcu

import cn.staynoob.cu.DefaultCu
import cn.staynoob.cu.base.DataJpaTest
import cn.staynoob.cu.fixture.source.hostfirst.BarEntity
import cn.staynoob.cu.fixture.source.hostfirst.BarSource
import cn.staynoob.cu.fixture.source.hostfirst.FooEntity
import cn.staynoob.cu.fixture.source.hostfirst.FooSource
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@DisplayName("source property test")
class SourcePropertyTest : DataJpaTest() {

    @Autowired
    private lateinit var cu: DefaultCu

    @Test
    @DisplayName("create test")
    fun test100() {
        val source = FooSource("foo", BarSource("bar"))
        val res = cu.create(source)
        val expected = FooEntity("foo", BarEntity("bar"))
        Assertions.assertThat(res).isEqualTo(expected)
    }

    @Test
    @DisplayName("update test")
    fun test200() {
        val foo = FooEntity("foo", BarEntity("foo"))
        val source = FooSource("bar", BarSource("bar"))
        cu.update(foo, source)
        val expected = FooEntity("bar", BarEntity("bar"))
        Assertions.assertThat(foo).isEqualTo(expected)
    }
}