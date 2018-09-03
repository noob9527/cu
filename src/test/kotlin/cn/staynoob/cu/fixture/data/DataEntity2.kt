package cn.staynoob.cu.fixture.data

import cn.staynoob.cu.fixture.AbstractEntity

data class DataEntity2(
        var foo: String,
        var source1: DataSource1
) : AbstractEntity()