package cn.staynoob.cu

import com.google.common.reflect.TypeToken
import kotlin.reflect.KClass

/**
 * T: must be instantiable
 */
abstract class AbstractCuSource<T : Any> : CuSource<T> {

    private val typeToken: TypeToken<T> = object : TypeToken<T>(this.javaClass) {}

    /**
     * Note: this trick only works if the type parameter T
     * is known in compile time
     */
    @Suppress("UNCHECKED_CAST")
    override val targetClass: KClass<T> = (typeToken.rawType as Class<T>).kotlin
}
