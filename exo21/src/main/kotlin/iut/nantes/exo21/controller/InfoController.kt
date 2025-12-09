package iut.nantes.exo21.controller

import iut.nantes.exo21.domain.Git
import iut.nantes.exo21.domain.Info
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class InfoController {
    @Value("\${custom.app-name}")
    private lateinit var appName: String
    @Value("\${custom.app-version}")
    private lateinit var appVersion: String
    @Value("\${custom.git.branch}")
    private lateinit var gitBranch: String
    @Value("\${custom.git.commit}")
    private lateinit var gitCommit: String
    @GetMapping("/api/v1/info")
    fun getInfo(): Info {
        return Info(
            appName = appName,
            appVersion = appVersion,
            git = Git(
                branch = gitBranch,
                commit = gitCommit
            )
        )
    }
}