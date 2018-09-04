package cn.staynoob.cu.fixture.source.hostfirst1

import cn.staynoob.cu.AbstractCuSource
import cn.staynoob.cu.fixture.source.hostfirst2.BarEntity3

data class BarSource2(
        var bar: String? = null
) : AbstractCuSource<BarEntity2>()