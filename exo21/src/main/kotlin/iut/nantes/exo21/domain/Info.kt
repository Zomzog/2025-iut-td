package iut.nantes.exo21.domain

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "custom")
data class Info(
    val appName: String,
    val appVersion: String,
    val git: Git,
)

data class Git(
    val branch: String,
    val commit: String,
)