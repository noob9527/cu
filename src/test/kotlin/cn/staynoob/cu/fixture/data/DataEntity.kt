package cn.staynoob.cu.fixture.data

import cn.staynoob.cu.fixture.AbstractEntity
import javax.persistence.Entity

@Entity
data class DataEntity(
        var foo: String
) : AbstractEntity()