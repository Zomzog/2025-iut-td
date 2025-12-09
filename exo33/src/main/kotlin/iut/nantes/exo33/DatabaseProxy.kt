package iut.nantes.exo33

import iut.nantes.exo33.controller.HumanDto
import iut.nantes.exo33.controller.PetDto
import iut.nantes.exo33.repository.HumanEntity
import iut.nantes.exo33.repository.HumanRepository
import org.springframework.stereotype.Service

@Service
class DatabaseProxy(val repository: HumanRepository) {
    fun savePet(pet: PetDto): PetDto {
        TODO()
    }

    fun findPetById(id: Int): PetDto? {
        TODO()
    }

    fun findAllPets(): List<PetDto> {
        TODO()
    }

    fun saveHuman(humanDto: HumanDto): HumanDto {
        val saved = repository.save(HumanEntity(
            humanId = humanDto.humanId,
            name = humanDto.name
        ))
        return saved.toDto()
    }

    fun findHumanById(id: Int): HumanDto? {
       return repository.findById(id).map { it.toDto() }.orElse(null)
    }

    fun findAllHuman(): List<HumanDto> {
        return repository.findAll().map { it.toDto() }
    }
}

fun HumanDto.toEntity(): HumanEntity = HumanEntity(
    humanId = this.humanId,
    name = this.name
)

fun HumanEntity.toDto(): HumanDto = HumanDto(
    humanId = this.humanId,
    name = this.name
)