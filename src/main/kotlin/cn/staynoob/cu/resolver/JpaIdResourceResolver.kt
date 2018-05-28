package cn.staynoob.cu.resolver

import cn.staynoob.cu.domain.IdReference
import cn.staynoob.cu.domain.Reference
import cn.staynoob.cu.exception.ReferenceNotFoundException
import cn.staynoob.cu.exception.UnresolvableReferenceException
import javax.persistence.EntityManager
import kotlin.reflect.KClass

class JpaIdResourceResolver(
        private val entityManager: EntityManager
) : ResourceResolver {
    override fun <T : Any> resolve(reference: Reference, targetClass: KClass<T>): T {
        if (reference !is IdReference<*>)
            throw UnresolvableReferenceException(reference::class, this::class)
        val id = reference.id
        return entityManager.find(targetClass.java, id)
                ?: throw ReferenceNotFoundException(reference)
    }
}