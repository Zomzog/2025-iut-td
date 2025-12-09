package iut.nantes.exo33.repository

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.springframework.data.jpa.repository.JpaRepository

interface HumanRepository: JpaRepository<HumanEntity, Int>

@Entity
data class HumanEntity(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val humanId: Int?,
    val name: String)
