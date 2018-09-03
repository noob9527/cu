package cn.staynoob.cu.fixture.reference

import cn.staynoob.cu.AbstractCuSource
import cn.staynoob.cu.domain.IntIdReference

data class ReferenceSource(
        var dumb: IntIdReference? = null
) : AbstractCuSource<ReferenceEntity>()