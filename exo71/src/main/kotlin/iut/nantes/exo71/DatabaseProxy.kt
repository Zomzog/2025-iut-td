package iut.nantes.exo71

import iut.nantes.exo71.controller.ContactDto
import iut.nantes.exo71.controller.HumanDto
import iut.nantes.exo71.controller.PetDto
import iut.nantes.exo71.controller.PetKind
import iut.nantes.exo71.repository.ContactEntity
import iut.nantes.exo71.repository.HumanEntity
import iut.nantes.exo71.repository.HumanRepository
import iut.nantes.exo71.repository.PetEntity
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
        val saved = repository.save(humanDto.toEntity())
        return saved.toDto()
    }

    fun findHumanById(id: Int): HumanDto? {
        return repository.findById(id).map { it.toDto() }.orElse(null)
    }

    fun findAllHuman(): List<HumanDto> {
        return repository.findAll()
            .map { it.toDto() }
    }

    fun deletePet(humanId: Int, petId: Int) {
        val human = repository.findById(humanId).orElseThrow { IllegalArgumentException("Human with ID $humanId not found") }
        human.pets.removeIf { it.petId == petId }
        repository.save(human)
    }

    fun HumanDto.toEntity(): HumanEntity = HumanEntity(
        humanId = this.humanId,
        name = this.name,
        contact = ContactEntity(
            contactId = null,
            email = this.contact.email
        ),
        creatorLogin = this.creatorLogin ?: throw IllegalArgumentException("Creator login cannot be null"),
        pets = this.pets.map { PetEntity(it.id, it.name, it.age, it.kind.name) }.toMutableList()
    )

    fun HumanEntity.toDto(): HumanDto = HumanDto(
        humanId = this.humanId,
        name = this.name,
        contact = ContactDto(
            email = this.contact.email
        ),
        creatorLogin = this.creatorLogin,
        pets = this.pets.map { PetDto(it.petId, it.name, it.age, PetKind.valueOf(it.kind)) }
    )
}