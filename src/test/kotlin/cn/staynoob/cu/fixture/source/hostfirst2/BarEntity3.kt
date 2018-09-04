package cn.staynoob.cu.fixture.source.hostfirst2

import cn.staynoob.cu.fixture.AbstractEntity
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
data class BarEntity3(
        @ManyToOne
        @JoinColumn
        var foo: FooEntity3,
        var bar: String
) : AbstractEntity()