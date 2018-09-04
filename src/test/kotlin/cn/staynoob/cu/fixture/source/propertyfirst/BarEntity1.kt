package cn.staynoob.cu.fixture.source.propertyfirst

import cn.staynoob.cu.fixture.AbstractEntity
import javax.persistence.Entity

@Entity
data class BarEntity1(
        var bar: String
) : AbstractEntity()