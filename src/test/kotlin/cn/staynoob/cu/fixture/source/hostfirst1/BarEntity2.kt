package cn.staynoob.cu.fixture.source.hostfirst1

import cn.staynoob.cu.fixture.AbstractEntity
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.OneToOne

@Entity
data class BarEntity2(
        @OneToOne
        @JoinColumn
        var foo: FooEntity2,
        var bar: String
) : AbstractEntity()