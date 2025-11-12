package iut.nantes.exo21.domain

data class Info(
    val appName: String,
    val appVersion: String,
    val git: Git,
)

data class Git(
    val branch: String,
    val commit: String,
)