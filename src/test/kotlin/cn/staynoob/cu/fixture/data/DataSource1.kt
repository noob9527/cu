package cn.staynoob.cu.fixture.data

import cn.staynoob.cu.AbstractCuSource

data class DataSource1(
        var foo: String? = null
) : AbstractCuSource<DataEntity1>()