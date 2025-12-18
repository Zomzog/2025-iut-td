package iut.nantes.exo33.controller

data class HumanDto(val humanId: Int?, val name: String, val contact: ContactDto, val pets: List<PetDto>, val creatorLogin: String? = null)

data class ContactDto(val email: String)
