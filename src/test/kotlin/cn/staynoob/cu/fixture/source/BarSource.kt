package cn.staynoob.cu.fixture.source

import cn.staynoob.cu.fixture.AbstractCuSource

data class BarSource(
        var bar: String? = null
) : AbstractCuSource<BarEntity>(BarEntity::class)