package cn.staynoob.cu.fixture.data

import cn.staynoob.cu.fixture.AbstractCuSource

data class DataSource(
        var foo: String? = null
) : AbstractCuSource<DataEntity>(DataEntity::class)