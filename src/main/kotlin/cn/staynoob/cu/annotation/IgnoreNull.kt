package cn.staynoob.cu.annotation

import java.lang.annotation.Inherited

@Inherited
@Target(
        AnnotationTarget.CLASS,
        AnnotationTarget.PROPERTY
)
annotation class IgnoreNull