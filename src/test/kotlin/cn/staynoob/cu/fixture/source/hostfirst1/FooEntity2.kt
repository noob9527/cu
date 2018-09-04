package cn.staynoob.cu.fixture.source.hostfirst1

import cn.staynoob.cu.fixture.AbstractEntity
import javax.persistence.Entity
import javax.persistence.OneToOne

@Entity
data class FooEntity2(
        var foo: String
) : AbstractEntity() {
    @OneToOne(mappedBy = "foo")
    var bar: BarEntity2? = null
}