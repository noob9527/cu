package cn.staynoob.cu.base

import cn.staynoob.cu.autoconfigure.CuAutoConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@DataJpaTest
@Transactional
@ImportAutoConfiguration(CuAutoConfiguration::class)
abstract class DataJpaTest : TestBase() {
    @Autowired
    protected lateinit var entityManager: EntityManager
}
