package iut.nantes

import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope

@Configuration
@ComponentScan(basePackages = ["iut.nantes"])
class AppConfig {
    @Bean
    fun userService(database: Database) = UserService(database)

    @Bean
    fun superUserService() = SuperUserService()
}