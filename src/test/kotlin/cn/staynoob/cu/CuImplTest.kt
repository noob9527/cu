package cn.staynoob.cu

import cn.staynoob.cu.fixture.AbstractEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import javax.persistence.Entity

internal class CuImplTest : DataJpaTest() {

    @Autowired
    private lateinit var cu: Cu

    @Entity
    class PresentEntity : AbstractEntity()

    private lateinit var presentEntity: PresentEntity

    @BeforeEach
    fun setUp() {
        presentEntity = PresentEntity()
        entityManager.persist(presentEntity)
        entityManager.flush()
    }

    @Entity
    class Test100(
            var name: String
    ) : AbstractEntity()

    data class TestDto100(
            var name: String?
    )

    @Test
    @DisplayName("create should copy plain data properties")
    fun test100() {
        val entity = Test100("foo")
        val dto = TestDto100("bar")
        val res = cu.create(Test100::class, dto)
        assertThat(res.name).isEqualTo("bar")
    }

    @Test
    @DisplayName("update should copy plain data properties")
    fun test200() {
        val entity = Test100("foo")
        val dto = TestDto100("bar")
        val res = cu.update(entity, dto)
        assertThat(res.name).isEqualTo("bar")
    }
}