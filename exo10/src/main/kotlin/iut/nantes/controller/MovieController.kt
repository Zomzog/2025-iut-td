package iut.nantes.controller

import iut.nantes.Database
import iut.nantes.FR_CA
import iut.nantes.FR_FR
import iut.nantes.Movie
import org.apache.tomcat.util.http.parser.AcceptLanguage
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.function.ServerRequest

@RestController
class MovieController(val database: Database) {
    @PostMapping("/api/movies")
    fun addMovie(@RequestBody movie: Movie) =
        if (database.getOne(movie.name) == null) {
            database.addMovie(movie)
                .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }
        } else {
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        }

    @GetMapping("/api/movies")
    fun findAll(@RequestParam(name = "rating") queryRating: List<Int>?,
                @RequestHeader(name= "Accept-Language", defaultValue = "en-EN") acceptLanguage: String ): List<Movie> {
       val all = database.findAll()
        return if (queryRating != null) {
             all.filter { it.rating in queryRating }
        } else {
            all
        }.map { translate(it, acceptLanguage) }
    }

    private fun translate(movie: Movie, acceptLanguage: String) =
        when (acceptLanguage.uppercase()) {
            "FR-FR" -> movie.copy(name = FR_FR.get(movie.name) ?: movie.name)
            "FR-CA" -> movie.copy(name = FR_CA .get(movie.name) ?: movie.name)
            else -> movie
        }

    @GetMapping("/api/movies/{name}")
    fun getOne(@PathVariable name: String) =
        database.getOne(name)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.status(HttpStatus.NOT_FOUND).build()

    @PutMapping("/api/movies/{name}")
    fun update(@PathVariable name: String, @RequestBody movie: Movie) =
        if (name != movie.name) {
            ResponseEntity.badRequest().body("Name in path and body must be the same")
        } else if (database.getOne(name) == null) {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Movie not found")
        } else {
            database.update(movie).let {
                ResponseEntity.ok(it)
            }
        }

    @DeleteMapping("/api/movies/{name}")
    fun delete(@PathVariable name: String): ResponseEntity<Unit> = database.getOne(name)?.let {
        database.delete(it.name)
        ResponseEntity.noContent().build()
    } ?: ResponseEntity.badRequest().build()
}