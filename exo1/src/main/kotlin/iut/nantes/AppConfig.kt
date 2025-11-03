package iut.nantes

import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope

@Configuration
class AppConfig {
    @Bean("theHashDatabase")
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    fun database() = HashDatabase()

    @Bean
    fun userService(database: Database) = UserService(database)

    @Bean
    fun superUserService() = SuperUserService()
}