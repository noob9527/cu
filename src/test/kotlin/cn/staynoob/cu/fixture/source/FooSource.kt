package cn.staynoob.cu.fixture.source

import cn.staynoob.cu.fixture.AbstractCuSource

data class FooSource(
        var foo: String? = null,
        var bar: BarSource? = null
) : AbstractCuSource<FooEntity>(FooEntity::class)