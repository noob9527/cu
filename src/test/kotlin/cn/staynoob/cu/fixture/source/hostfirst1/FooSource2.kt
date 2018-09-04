package cn.staynoob.cu.fixture.source.hostfirst1

import cn.staynoob.cu.AbstractCuSource
import cn.staynoob.cu.annotation.NestedCuSource

data class FooSource2(
        var foo: String? = null,
        @NestedCuSource(backReference = "foo")
        var bar: BarSource2? = null
) : AbstractCuSource<FooEntity2>()