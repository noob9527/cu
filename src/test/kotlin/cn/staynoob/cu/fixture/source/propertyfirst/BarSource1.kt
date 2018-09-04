package cn.staynoob.cu.fixture.source.propertyfirst

import cn.staynoob.cu.AbstractCuSource

data class BarSource1(
        var bar: String? = null
) : AbstractCuSource<BarEntity1>()