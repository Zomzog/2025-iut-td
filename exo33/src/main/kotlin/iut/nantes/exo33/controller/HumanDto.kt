package iut.nantes.exo33.controller

data class HumanDto(val humanId: Int?, val name: String, val contact: ContactDto)

data class ContactDto(val email: String)
