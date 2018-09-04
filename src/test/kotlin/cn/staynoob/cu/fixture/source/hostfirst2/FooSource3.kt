package cn.staynoob.cu.fixture.source.hostfirst2

import cn.staynoob.cu.AbstractCuSource
import cn.staynoob.cu.annotation.NestedCuSource

data class FooSource3(
        var foo: String? = null,
        @NestedCuSource(backReference = "foo")
        var bars: List<BarSource3>? = null
) : AbstractCuSource<FooEntity3>()