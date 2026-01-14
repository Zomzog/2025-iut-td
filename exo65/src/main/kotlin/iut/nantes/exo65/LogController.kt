package iut.nantes.exo65

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class LogController(val randomService: RandomService) {

    @GetMapping("/log")
    fun logSomething(): ResponseEntity<Double?> {
        val random = randomService.randomThrow()
        return ResponseEntity.ok(random)
    }
}