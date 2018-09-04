package cn.staynoob.cu.fixture.source.hostfirst2

import cn.staynoob.cu.annotation.NestedCuSource
import cn.staynoob.cu.fixture.AbstractEntity
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
class FooEntity3(
        var foo: String,
        @NestedCuSource(backReference = "foo")
        @OneToMany(mappedBy = "foo")
        var bars: List<BarEntity3> = listOf()
) : AbstractEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FooEntity3

        if (foo != other.foo) return false

        return true
    }

    override fun hashCode(): Int {
        return foo.hashCode()
    }
}