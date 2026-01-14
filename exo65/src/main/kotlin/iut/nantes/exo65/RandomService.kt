package iut.nantes.exo65

import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class RandomService {
    fun randomThrow(): Double {
        val random = Math.random()
        if (random < 0.5) {
            throw RuntimeException("Random exception for testing purposes.")
        } else {
            return random
        }
    }

    fun random(int: Int) = Random.nextInt( int)
}