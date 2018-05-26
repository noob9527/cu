package cn.staynoob.cu

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@DataJpaTest
@Transactional
abstract class DataJpaTest : TestBase() {
    @Autowired
    protected lateinit var entityManager: EntityManager
}
