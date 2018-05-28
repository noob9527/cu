package cn.staynoob.cu.fixture.reference

import cn.staynoob.cu.fixture.AbstractEntity
import javax.persistence.Entity

@Entity
data class DumbEntity(
        val name: String
) : AbstractEntity()