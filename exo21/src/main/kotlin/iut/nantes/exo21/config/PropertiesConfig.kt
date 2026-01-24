package iut.nantes.exo21.config

import iut.nantes.exo21.domain.Info
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(Info::class)
class PropertiesConfig {
}