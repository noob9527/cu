package cn.staynoob.cu.fixture.source.propertyfirst

import cn.staynoob.cu.fixture.AbstractEntity
import javax.persistence.Entity
import javax.persistence.OneToOne

@Entity
data class FooEntity1(
        var foo: String,
        @OneToOne
        var bar: BarEntity1
) : AbstractEntity()