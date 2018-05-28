package cn.staynoob.cu.fixture.source

import cn.staynoob.cu.fixture.AbstractEntity
import javax.persistence.Entity
import javax.persistence.OneToOne

@Entity
data class FooEntity(
        var foo: String,
        @OneToOne
        var bar: BarEntity
) : AbstractEntity()