package iut.nantes.exo33.repository

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import org.springframework.data.jpa.repository.JpaRepository

interface HumanRepository: JpaRepository<HumanEntity, Int>

@Entity
data class HumanEntity(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val humanId: Int?,
    val name: String,
    @OneToOne(cascade = [(CascadeType.ALL)])
    @JoinColumn(name = "contact_id")
    val contact: ContactEntity,
    @OneToMany(cascade = [(CascadeType.ALL)], orphanRemoval = true)
    @JoinColumn(name = "human_id")
    val pets: MutableList<PetEntity> = mutableListOf(),
    )

@Entity
data class ContactEntity(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val contactId: Int?,
    val email: String,
    @OneToOne(mappedBy = "contact")
    val human: HumanEntity? = null,
    )

@Entity
data class PetEntity(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val petId: Int?,
    val name: String,
    val age: Int,
    val kind: String,
    @ManyToOne
    @JoinColumn(name = "human_id")
    val human: HumanEntity? = null,
    )