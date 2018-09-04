package cn.staynoob.cu.fixture.source.propertyfirst

import cn.staynoob.cu.AbstractCuSource
import cn.staynoob.cu.annotation.NestedCuSource

data class FooSource1(
        var foo: String? = null,
        @NestedCuSource
        var bar: BarSource1? = null
) : AbstractCuSource<FooEntity1>()