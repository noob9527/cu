package cn.staynoob.cu.annotation

@Target(AnnotationTarget.PROPERTY)
annotation class NestedCuSource(
        val backReference: String = ""
)
