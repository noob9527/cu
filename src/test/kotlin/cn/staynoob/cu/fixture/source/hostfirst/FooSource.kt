package cn.staynoob.cu.fixture.source.hostfirst

import cn.staynoob.cu.AbstractCuSource
import cn.staynoob.cu.annotation.NestedCuSource

data class FooSource(
        var foo: String? = null,
        @NestedCuSource
        var bar: BarSource? = null
) : AbstractCuSource<FooEntity>()