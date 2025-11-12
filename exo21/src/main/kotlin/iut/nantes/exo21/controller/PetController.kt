package iut.nantes.exo21.controller

import iut.nantes.exo21.domain.AgeRange
import iut.nantes.exo21.domain.PetId
import iut.nantes.exo21.errors.ImATeapotException
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import jakarta.validation.Valid
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import kotlin.reflect.KClass

@Validated
@RestController
class PetController(val database : MutableMap<Int, PetDto> = mutableMapOf()){

    @GetMapping("/api/v1/pets/{petId}")
    fun getPet(@PathVariable @PetId petId: Int) = database[petId]?.let {
        ResponseEntity.ok(it)
    } ?: ResponseEntity.notFound().build()

    @PostMapping("/api/v1/pets")
    fun createPet(@RequestBody @Valid pet: PetDto): ResponseEntity<PetDto> {
        val next = (database.keys.maxOrNull() ?: 0) + 1
        val withId = pet.copy(id = next)
        database[next] = withId
        return ResponseEntity.status(HttpStatus.CREATED).body(withId)
    }

    @PutMapping("/api/v1/pets/{petId}")
    fun updatePet(@RequestBody pet: PetDto, @PathVariable @PetId petId: Int): ResponseEntity<PetDto> {
        if (pet.id != petId) {
            throw IllegalArgumentException("Pet ID in path and body do not match")
        }
        val previous = database[petId]
        if (previous == null) {
            throw ImATeapotException()
        } else {
            database[petId] = pet
        }
        return ResponseEntity.ok(pet)
    }

    @GetMapping("/api/v1/pets")
    fun getPets(filters: AgeRange): ResponseEntity<List<PetDto>> {
       var result: List<PetDto> = database.values.toList()
        if (filters.minAge != null) result = result.filter { it.age >= filters.minAge }
        if (filters.maxAge != null) result = result.filter { it.age <= filters.maxAge }
        return ResponseEntity.ok(result)
    }
}

