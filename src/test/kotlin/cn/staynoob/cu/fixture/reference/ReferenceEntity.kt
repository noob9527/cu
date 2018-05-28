package cn.staynoob.cu.fixture.reference

import cn.staynoob.cu.fixture.AbstractEntity
import javax.persistence.Entity
import javax.persistence.ManyToOne

@Entity
data class ReferenceEntity(
        @ManyToOne
        var dumb: DumbEntity
) : AbstractEntity()