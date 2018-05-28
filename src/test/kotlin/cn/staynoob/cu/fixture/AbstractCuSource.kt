package cn.staynoob.cu.fixture

import cn.staynoob.cu.CuSource
import kotlin.reflect.KClass

abstract class AbstractCuSource<T : Any>(
        override val targetClass: KClass<T>
) : CuSource<T>