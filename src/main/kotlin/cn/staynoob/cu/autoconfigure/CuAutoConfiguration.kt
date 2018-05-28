package cn.staynoob.cu.autoconfigure

import cn.staynoob.cu.DefaultCu
import cn.staynoob.cu.resolver.JpaIdResourceResolver
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManager

@Configuration
class CuAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    fun cu(applicationContext: ApplicationContext): DefaultCu {
        return DefaultCu(applicationContext)
    }

    @Bean
    fun jpaIdResourceResolver(entityManager: EntityManager): JpaIdResourceResolver {
        return JpaIdResourceResolver(entityManager)
    }
}