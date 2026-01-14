package iut.nantes.exo60

import assertk.assertThat
import assertk.assertions.contains
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import javax.sql.DataSource

@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("jdbc")
class Exo62Test {

    @Autowired
    lateinit var dataSource: DataSource

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    lateinit var flywaySchemaHistoryRepository: FlywaySchemaHistoryRepository

    @Test
    fun exo62() {
        val cols = findAllColumns()
        assertThat(cols).contains("id" to "serial")
        assertThat(cols).contains("name" to "varchar")
        assertThat(cols).contains("color" to "varchar")
        assertThat(cols).contains("age" to "int4")
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