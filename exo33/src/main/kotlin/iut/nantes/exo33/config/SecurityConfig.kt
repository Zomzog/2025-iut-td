package iut.nantes.exo33.config

import org.springframework.boot.webservices.client.WebServiceMessageSenderFactory.http
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.provisioning.JdbcUserDetailsManager
import org.springframework.security.provisioning.UserDetailsManager
import javax.sql.DataSource


@EnableWebSecurity
@Configuration
class SecurityConfig {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            csrf { disable() }
            headers { frameOptions { disable() } }
            authorizeHttpRequests {
                authorize (HttpMethod.GET, "/**", permitAll)
                authorize("/api/v1/humans/*/pets/*", hasRole("ADMIN"))
                authorize(anyRequest, permitAll) // for h2-console
            }
            httpBasic { }
            formLogin { }
        }
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    @Profile("dev")
    fun userDetailService(passwordEncoder: PasswordEncoder): UserDetailsManager {
        val admin = User.withUsername("admin")
            .password(passwordEncoder.encode("1234"))
            .roles("ADMIN")
            .build()
        val demo = User.withUsername("login")
            .password(passwordEncoder.encode("password"))
            .roles("ADMIN")
            .build()
        return InMemoryUserDetailsManager(admin, demo)
    }

    @Bean
    @Profile("!dev")
    fun jpaUserDetailService(dataSource: DataSource,
                          passwordEncoder: PasswordEncoder): UserDetailsManager {
        val user1 = User.withUsername("u1")
            .password(passwordEncoder.encode("pw"))
            .roles("USER")
            .build()
        return JdbcUserDetailsManager(dataSource).apply {
            createUser(user1)
        }
    }
}