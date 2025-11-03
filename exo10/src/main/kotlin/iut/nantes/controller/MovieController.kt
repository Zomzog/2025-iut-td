package iut.nantes.controller

import iut.nantes.Database
import iut.nantes.Movie
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MovieController(val database: Database){
    @PostMapping("/api/movies")
    fun addMovie(@RequestBody movie: Movie) =
        if (database.getOne(movie.name) == null) {
            database.addMovie(movie)
                .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }
        } else {
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        }

    @GetMapping("/api/movies")
    fun findAll() = database.findAll()

    @GetMapping("/api/movies/{name}")
    fun getOne(@PathVariable name: String) =
        database.getOne(name)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.status(HttpStatus.NOT_FOUND).build()
}