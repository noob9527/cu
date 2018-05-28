package cn.staynoob.cu.fixture.source

import cn.staynoob.cu.fixture.AbstractEntity
import javax.persistence.Entity

@Entity
data class BarEntity(
        var bar: String
) : AbstractEntity()