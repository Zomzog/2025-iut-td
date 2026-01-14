package iut.nantes.exo60

import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.data.jpa.repository.JpaRepository

interface PonyRepository: JpaRepository<PonyEntity, Long> {
}

@Entity
data class PonyEntity(
    @Id
    val id: Long,
    val name: String,
    val color: String,
    val age: Int,
)

