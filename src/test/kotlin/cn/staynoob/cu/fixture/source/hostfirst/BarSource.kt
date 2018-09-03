package cn.staynoob.cu.fixture.source.hostfirst

import cn.staynoob.cu.AbstractCuSource

data class BarSource(
        var bar: String? = null
) : AbstractCuSource<BarEntity>()