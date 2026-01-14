package iut.nantes.exo60

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isPresent
import assertk.assertions.prop
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import javax.sql.DataSource

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("SC")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
class Exo64Test {

    companion object Companion {
        @Container
        @ServiceConnection
        val pg = PostgreSQLContainer("postgres:latest")
    }

    @Autowired
    lateinit var dataSource: DataSource

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    lateinit var flywaySchemaHistoryRepository: FlywaySchemaHistoryRepository

    @Test
    fun exo64() {
        val cols = findAllColumns()
        assertThat(cols).contains("id" to "serial")
        assertThat(cols).contains("name" to "varchar")
        assertThat(cols).contains("color" to "varchar")
        assertThat(cols).contains("age" to "int4")

        assertThat(flywaySchemaHistoryRepository.findAll()).hasSize(2)
        val h = flywaySchemaHistoryRepository.findById(2)
        assertThat(h).isPresent()
            .prop(FlywaySchemaHistoryEntity::description)
            .isEqualTo("add age")
    }

    private fun findAllColumns(): MutableMap<String, String> {
        val cols = mutableMapOf<String, String>()
        dataSource.connection.use { conn ->
            val md = conn.metaData
            val rs = md.getColumns(null, null, "pony", null)
            rs.use {
                while (it.next()) {
                    val name = it.getString("COLUMN_NAME")
                    val type = it.getString("TYPE_NAME")
                    cols[name] = type
                }
            }
        }
        return cols
    }

}