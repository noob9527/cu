package cn.staynoob.cu

import cn.staynoob.cu.annotation.Ignore
import cn.staynoob.cu.annotation.IgnoreNull
import cn.staynoob.cu.annotation.NestedCuSource
import cn.staynoob.cu.domain.Reference
import cn.staynoob.cu.exception.ResolverNotFoundException
import cn.staynoob.cu.resolver.ResourceResolver
import org.springframework.context.ApplicationContext
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty1
import kotlin.reflect.KVisibility
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters

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
        return create(source, mapOf(), null)
    }

    private fun <T : Any> create(
            source: CuSource<T>,
            additionParam: Map<String, Any?> = mapOf(),
            constructor: KFunction<T>? = null
    ): T {
        val ctor = constructor ?: source.targetClass.primaryConstructor
                ?: throw IllegalArgumentException("class should have a primary constructor: ${source.targetClass}")

        val argMap = extractArguments(source)

        val ctorParam = ctor.valueParameters
                .map { it.name!! }
                .toSet()
        val requiredCtorParam = Utils.getRequiredParameters(ctor)

        val (ctorArgs, restArgs) = argMap
                .toList()
                .partition {
                    val annotation = it.first.findAnnotation<NestedCuSource>()
                    val requireHost = annotation != null && annotation.backReference.isNotEmpty()
                    if (requiredCtorParam.contains(it.first.name) && requireHost) {
                        throw IllegalArgumentException(
                                "cannot resolve property:${it.first.name} which need a ${source.targetClass} instance"
                        )
                    }
                    ctorParam.contains(it.first.name) && !requireHost
                }

        val tmp1 = resolveArguments(
                source,
                ctorArgs.toMap(),
                null,
                resolvers
        ) + additionParam
        val ctorArgMap = source.beforeCreateInstance(tmp1)

        val instance = Utils.createInstance(ctor, ctorArgMap)

        val tmp2 = resolveArguments(
                source,
                restArgs.toMap(),
                instance,
                resolvers
        )
        val restArgMap = source.beforeUpdateInstance(instance, tmp2)

        update(instance, restArgMap)

        return instance
    }

    /**
     * this method won't call entityManager.merge for you
     */
    override fun <T : Any> update(target: T, source: CuSource<T>) {
        val argMap = extractArguments(source)

        val tmp = resolveArguments(
                source,
                argMap,
                target,
                resolvers
        )
        val args = source.beforeUpdateInstance(target, tmp)

        update(target, args)
    }

    private fun <T : Any> update(target: T, args: Map<String, Any?>) {
        Utils.assignProperties(target, args)
    }

    internal fun extractArguments(
            source: CuSource<*>
    ): Map<KProperty1<out CuSource<*>, Any?>, Any?> {
        val ignoreNull = source::class.isAnnotationPresent<IgnoreNull>()

        @Suppress("UnnecessaryVariable")
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
                .toMap()

        return props
    }

    internal fun resolveArguments(
            source: CuSource<*>,
            args: Map<KProperty1<out CuSource<*>, Any?>, Any?>,
            host: Any?,
            resolvers: Map<KClass<out ResourceResolver>, ResourceResolver> = mapOf()
    ): Map<String, Any?> {
        return args
                .map { (key, value) ->
                    key.name to mapValue(key, value, source, host, resolvers)
                }.toMap()
    }

    private fun mapValue(
            key: KProperty1<out CuSource<*>, Any?>,
            value: Any?,
            source: CuSource<*>,
            host: Any?,
            resolvers: Map<KClass<out ResourceResolver>, ResourceResolver> = mapOf()
    ): Any? {
        return if (value is Collection<*>) {
            when (value) {
                is List<*> -> {
                    value.map { mapSingleValue(key, it, source, host, resolvers) }.toMutableList()
                }
                is Set<*> -> {
                    value.map { mapSingleValue(key, it, source, host, resolvers) }.toMutableSet()
                }
                else -> {
                    throw IllegalArgumentException("for the time being, only Set and List collection are supported")
                }
            }
        } else {
            mapSingleValue(key, value, source, host, resolvers)
        }
    }

    private fun mapSingleValue(
            key: KProperty1<out CuSource<*>, Any?>,
            value: Any?,
            source: CuSource<*>,
            host: Any?,
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
                    val annotation = key.findAnnotation<NestedCuSource>()!!
                    // call create method recursively
                    if (annotation.backReference.isEmpty()) {
                        this.create(value)
                    } else {
                        this.create(value, mapOf(annotation.backReference to host))
                    }
                } else {
                    value
                }
            }
        }
    }

}