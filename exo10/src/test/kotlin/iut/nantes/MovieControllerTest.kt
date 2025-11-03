package iut.nantes

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest
class MovieControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockkBean
    lateinit var database: Database

    @Test
    fun list() {
        // GIVEN
        every { database.findAll() } returns listOf(Movie("The Dark Knight", 2008, 94, listOf("VO")))
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
        every {database.getOne("The Dark Knight") } returns null
        every { database.addMovie(any()) } answers { firstArg<Movie>() }
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