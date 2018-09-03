package cn.staynoob.cu.defaultcu

import cn.staynoob.cu.DefaultCu
import cn.staynoob.cu.base.DataJpaTest
import cn.staynoob.cu.domain.IntIdReference
import cn.staynoob.cu.fixture.reference.DumbEntity
import cn.staynoob.cu.fixture.reference.ReferenceEntity
import cn.staynoob.cu.fixture.reference.ReferenceSource
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@DisplayName("reference property test")
class ReferencePropertyTest : DataJpaTest() {

    @Autowired
    private lateinit var cu: DefaultCu

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
        Assertions.assertThat(res)
                .isEqualTo(ReferenceEntity(dumb1))
    }

    @Test
    @DisplayName("update test")
    fun test200() {
        val foo = ReferenceEntity(dumb1)
        val source = ReferenceSource(IntIdReference(dumb2.id))
        cu.update(foo, source)
        Assertions.assertThat(foo)
                .isEqualTo(ReferenceEntity(dumb2))
    }
}