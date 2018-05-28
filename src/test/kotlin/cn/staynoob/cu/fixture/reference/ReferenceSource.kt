package cn.staynoob.cu.fixture.reference

import cn.staynoob.cu.domain.IntIdReference
import cn.staynoob.cu.fixture.AbstractCuSource

data class ReferenceSource(
        var dumb: IntIdReference? = null
) : AbstractCuSource<ReferenceEntity>(ReferenceEntity::class)