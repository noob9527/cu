package cn.staynoob.cu.fixture.source.hostfirst2

import cn.staynoob.cu.AbstractCuSource

data class BarSource3(
        var bar: String? = null
) : AbstractCuSource<BarEntity3>()