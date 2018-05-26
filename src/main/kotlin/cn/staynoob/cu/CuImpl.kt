package cn.staynoob.cu

import org.springframework.context.ApplicationContext
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class CuImpl(
        private val applicationContext: ApplicationContext
) : Cu {
    override fun <T : Any> create(targetClass: KClass<T>, source: Any, constructor: KFunction<T>?): T {
        TODO("not implemented")
    }

    override fun <T : Any> update(targetObj: T, source: Any): T {
        TODO("not implemented")
    }
}