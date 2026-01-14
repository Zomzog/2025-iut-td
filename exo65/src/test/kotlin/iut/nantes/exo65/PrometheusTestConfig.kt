package iut.nantes.exo65

import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class PrometheusTestConfig {
    @Bean
    fun prometheusMeterRegistry(): PrometheusMeterRegistry =
        PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
}