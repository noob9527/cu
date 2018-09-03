package cn.staynoob.cu.fixture.data

import cn.staynoob.cu.AbstractCuSource

data class DataSource2(
        var foo: String? = null,
        var source1: DataSource1? = null
) : AbstractCuSource<DataEntity2>()