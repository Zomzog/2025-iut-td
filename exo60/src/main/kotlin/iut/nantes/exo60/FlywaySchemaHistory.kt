package iut.nantes.exo60

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Minimal mapping for Flyway's schema history table.
 * Only a subset of columns is mapped (the common ones) so JPA can read the rows.
 */
@Entity
@Table(name = "flyway_schema_history")
data class FlywaySchemaHistoryEntity(
    @Id
    @Column(name = "installed_rank")
    val installedRank: Int,

    @Column(name = "version")
    val version: String? = null,

    @Column(name = "description")
    val description: String? = null,

    @Column(name = "type")
    val type: String? = null,

    @Column(name = "script")
    val script: String? = null,

    @Column(name = "checksum")
    val checksum: Int? = null,

    @Column(name = "installed_by")
    val installedBy: String? = null,

    @Column(name = "installed_on")
    val installedOn: LocalDateTime? = null,

    @Column(name = "success")
    val success: Boolean? = null,
)

interface FlywaySchemaHistoryRepository : JpaRepository<FlywaySchemaHistoryEntity, Int> {

    fun findTopByOrderByInstalledRankDesc(): FlywaySchemaHistoryEntity?
    fun findBySuccess(success: Boolean): List<FlywaySchemaHistoryEntity>
}

