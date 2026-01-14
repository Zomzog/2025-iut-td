package iut.nantes.exo65

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class CacheController(private val randomService: RandomService) {
    @GetMapping("/cache")
    fun getRandomValue(@RequestParam i: Int ): ResponseEntity<Int> {
        val randomValue = randomService.random(i)
        return ResponseEntity.ok(randomValue)
    }
}