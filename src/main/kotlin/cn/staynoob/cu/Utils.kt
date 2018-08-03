package cn.staynoob.cu

import org.apache.log4j.Logger
import kotlin.reflect.*
import kotlin.reflect.full.*
import kotlin.reflect.jvm.jvmErasure


inline fun <reified T : Annotation> KAnnotatedElement.isAnnotationPresent() = this.findAnnotation<T>() != null

object Utils {
    private val log = Logger.getLogger(Cu::class.java)

    internal fun <T : Any> createInstance(
            targetClazz: KClass<T>,
            args: Map<String, Any?>,
            constructor: KFunction<T>? = null
    ): T {
        val func: KFunction<T> = constructor ?: targetClazz.primaryConstructor
        ?: throw IllegalArgumentException("class should have a primary constructor: $targetClazz")

        // create instance
        val argMap = func.valueParameters
                .map { it to args[it.name] }
                .toMap()

        val instance = func.callBy(argMap)

        val others = args
                .filter { entry ->
                    !argMap.keys.map { it.name }.contains(entry.key)
                }

        assignProperties(instance, others)

        return instance
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

