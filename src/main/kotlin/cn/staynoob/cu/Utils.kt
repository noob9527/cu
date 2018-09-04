package cn.staynoob.cu

import org.apache.log4j.Logger
import kotlin.reflect.*
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmErasure


inline fun <reified T : Annotation> KAnnotatedElement.isAnnotationPresent() = this.findAnnotation<T>() != null

object Utils {
    private val log = Logger.getLogger(Cu::class.java)

    internal fun <T : Any> getRequiredParameters(
            func: KFunction<T>
    ): Set<String> {
        return func.valueParameters
                .asSequence()
                .filter { !it.isOptional && !it.type.isMarkedNullable }
                .map { it.name!! }
                .toSet()
    }

    internal fun <T : Any> createInstance(
            constructor: KFunction<T>,
            args: Map<String, Any?>
    ): T {
        // create instance
        val argMap = constructor.valueParameters
                .map {
                    it to args[it.name]
                }
                .filter {
                    it.second != null || !it.first.isOptional
                }.toMap()

        return constructor.callBy(argMap)
    }

    internal fun assignProperties(instance: Any, argMap: Map<String, Any?>) {
        val mutableProps = instance::class.memberProperties
                .filterIsInstance<KMutableProperty<*>>()
        for (entry in argMap) {
            val prop = mutableProps
                    .firstOrNull { it.name == entry.key }
                    ?: continue

            if (prop.setter.visibility != KVisibility.PUBLIC) continue

            try {
                prop.setter.call(instance, entry.value)
            } catch (e: IllegalArgumentException) {
                if (log.isWarnEnabled) {
                    log.warn("value:'${entry.value}' can not be assigned to property:'${prop.name}', which expect a '${prop.returnType}' type")
                }
            }
        }
    }

    /**
     * if target class doesn't have given property return null
     * if target class has given property and its class isn't subClass of Collection return its class
     * if its class is subClass of Collection, try to find its element class
     */
    internal fun resolveTargetClass(target: KClass<out Any>, propName: String): KClass<out Any>? {
        val prop = target.memberProperties
                .firstOrNull { it.name == propName }

        val propType = prop?.returnType
        val propClass = propType?.jvmErasure ?: return null

        if (!propClass.isSubclassOf(Collection::class)) return propClass
        if (propType.arguments.isEmpty()) return null
        return propType.arguments[0].type?.jvmErasure
    }

}

