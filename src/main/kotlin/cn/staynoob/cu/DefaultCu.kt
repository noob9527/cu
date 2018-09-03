package cn.staynoob.cu

import cn.staynoob.cu.annotation.Ignore
import cn.staynoob.cu.annotation.IgnoreNull
import cn.staynoob.cu.annotation.NestedCuSource
import cn.staynoob.cu.domain.Reference
import cn.staynoob.cu.exception.ResolverNotFoundException
import cn.staynoob.cu.resolver.ResourceResolver
import org.springframework.context.ApplicationContext
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

open class DefaultCu(
        applicationContext: ApplicationContext
) : Cu {

    private val resolvers = applicationContext.getBeansOfType(ResourceResolver::class.java)
            .map { it.value::class to it.value }
            .toMap()

    /**
     * this method won't call entityManager.persist for you
     */
    override fun <T : Any> create(source: CuSource<T>): T {
        val argMap = extractArguments(source, resolvers)
        return Utils.createInstance(source.targetClass, argMap)
    }

    /**
     * this method won't call entityManager.merge for you
     */
    override fun <T : Any> update(target: T, source: CuSource<T>) {
        val argMap = extractArguments(source, resolvers)
        Utils.assignProperties(target, argMap)
    }

    internal fun extractArguments(
            source: CuSource<*>,
            resolvers: Map<KClass<out ResourceResolver>, ResourceResolver> = mapOf()
    ): Map<String, Any?> {
        val ignoreNull = source::class.isAnnotationPresent<IgnoreNull>()

        val props = source::class.memberProperties
                .asSequence()
                .filter { !CuSource.properties.contains(it.name) }  // ignore 'CuSource' properties
                .filter { it.visibility != KVisibility.PRIVATE }    // ignore private properties
                .filter { !it.isAnnotationPresent<Ignore>() }
                .map { it to it.getter.call(source) }
                .filter {
                    it.second != null
                            || (!it.first.isAnnotationPresent<IgnoreNull>() && !ignoreNull)
                }

        val argMap = props
                .map { (key, value) ->
                    key to mapValue(key, value, source, resolvers)
                }.toMap()

        return source.toArguments(argMap.mapKeys { it.key.name })
    }

    private fun mapValue(
            key: KProperty1<out CuSource<*>, Any?>,
            value: Any?,
            source: CuSource<*>,
            resolvers: Map<KClass<out ResourceResolver>, ResourceResolver> = mapOf()
    ): Any? {
        return if (value is Collection<*>) {
            when (value) {
                is List<*> -> {
                    value.map { mapSingleValue(key, it, source, resolvers) }.toMutableList()
                }
                is Set<*> -> {
                    value.map { mapSingleValue(key, it, source, resolvers) }.toMutableSet()
                }
                else -> {
                    throw IllegalArgumentException("for the time being, only Set and List collection are supported")
                }
            }
        } else {
            mapSingleValue(key, value, source, resolvers)
        }
    }

    private fun mapSingleValue(
            key: KProperty1<out CuSource<*>, Any?>,
            value: Any?,
            source: CuSource<*>,
            resolvers: Map<KClass<out ResourceResolver>, ResourceResolver> = mapOf()
    ): Any? {
        return when (value) {
            is Reference -> {
                val targetClass = value.getReferenceClass()
                        ?: Utils.resolveTargetClass(source.targetClass, key.name)
                        ?: throw IllegalArgumentException("unable to resolve the actual class of property:'$key'")
                val resolver = resolvers[value.resolver]
                        ?: throw ResolverNotFoundException(value.resolver)
                resolver.resolve(value, targetClass)
            }
            else -> {
                if (value is CuSource<*> && key.isAnnotationPresent<NestedCuSource>()) {
                    // call create method recursively
                    this.create(value)
                } else {
                    value
                }
            }
        }
    }

}