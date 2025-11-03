package iut.nantes

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

/**
 *
 *
 *
 * FOR EXO 19
 *
 *
 *
 *
 */
@AutoConfigureMockMvc
@SpringBootTest
class MovieControllerTestIT {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var database: Database

    @Test
    fun list() {
        // GIVEN
        database.addMovie(Movie("The Dark Knight", 2008, 94, listOf("VO")))
        // WHEN
       mockMvc.get("/api/movies")
            .andExpect {
                // THEN
                status { isOk() }
                content { contentType("application/json") }
                jsonPath("$[0].name") { value("The Dark Knight") }
            }
    }

    @Test
    fun add() {
        // GIVEN
        // WHEN
        mockMvc.post("/api/movies")
        {
            contentType = MediaType.APPLICATION_JSON
            content = """{
                "name": "The Dark Knight",
                "releaseDate": 2008,
                "rating": 94,
                "languages": ["VO"]
            }"""
        }
            .andExpect {
                // THEN
                status { isCreated() }
                content { contentType("application/json") }
                jsonPath("$.name") { value("The Dark Knight") }
            }
    }
}