package cn.staynoob.cu.fixture

import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class AbstractEntity {
    @Id
    @GeneratedValue
    var id: Int? = null
}