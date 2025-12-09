package iut.nantes.exo21.controller

import iut.nantes.exo21.domain.Git
import iut.nantes.exo21.domain.Info
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class InfoController(val infoProperties: Info) {
    @GetMapping("/api/v1/info")
    fun getInfo(): Info {
        return infoProperties
    }
}