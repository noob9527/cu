package cn.staynoob.cu.defaultcu

import cn.staynoob.cu.DefaultCu
import cn.staynoob.cu.base.DataJpaTest
import cn.staynoob.cu.fixture.data.DataEntity1
import cn.staynoob.cu.fixture.data.DataEntity2
import cn.staynoob.cu.fixture.data.DataSource1
import cn.staynoob.cu.fixture.data.DataSource2
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@DisplayName("data property test")
class DataPropertyTest : DataJpaTest() {

    @Autowired
    private lateinit var cu: DefaultCu

    @Nested
    @DisplayName("case 1")
    inner class Case1Test {
        @Test
        @DisplayName("create test")
        fun test100() {
            val source = DataSource1("foo")
            val res = cu.create(source)
            Assertions.assertThat(res).isEqualTo(DataEntity1("foo"))
        }

        @Test
        @DisplayName("update test")
        fun test200() {
            val foo = DataEntity1("foo")
            val source = DataSource1("bar")
            cu.update(foo, source)
            Assertions.assertThat(foo).isEqualTo(DataEntity1("bar"))
        }
    }

    @Nested
    @DisplayName("case 2(nested CuSource instance without NestedCuSource annotation should be treated as data property)")
    inner class Case2Test {
        @Test
        @DisplayName("create test")
        fun test100() {
            val source = DataSource2("foo", DataSource1("foo"))
            val res = cu.create(source)
            Assertions.assertThat(res).isEqualTo(DataEntity2("foo", DataSource1("foo")))
        }

        @Test
        @DisplayName("update test")
        fun test200() {
            val foo = DataEntity2("foo", DataSource1("foo"))
            val source = DataSource2("bar", DataSource1("bar"))
            cu.update(foo, source)
            Assertions.assertThat(foo).isEqualTo(DataEntity2("bar", DataSource1("bar")))
        }
    }

}