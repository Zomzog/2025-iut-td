package iut.nantes.exo60

import assertk.assertThat
import assertk.assertions.contains
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import javax.sql.DataSource

@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext
@ActiveProfiles("h2")
class Exo60Test {

    @Autowired
    lateinit var dataSource: DataSource

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    lateinit var flywaySchemaHistoryRepository: FlywaySchemaHistoryRepository

    @Test
    fun exo60() {
        val cols = findAllColumns()
        assertThat(cols).contains("ID" to "INTEGER")
        assertThat(cols).contains("NAME" to "CHARACTER VARYING")
    }

    @Test
    fun exo61() {
        val cols = findAllColumns()
        assertThat(cols).contains("ID" to "INTEGER")
        assertThat(cols).contains("NAME" to "CHARACTER VARYING")
        assertThat(cols).contains("COLOR" to "CHARACTER VARYING")
        assertThat(cols).contains("AGE" to "INTEGER")
    }

    private fun findAllColumns(): MutableMap<String, String> {
        val cols = mutableMapOf<String, String>()
        dataSource.connection.use { conn ->
            val md = conn.metaData
            val rs = md.getColumns(null, null, "PONY", null)
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