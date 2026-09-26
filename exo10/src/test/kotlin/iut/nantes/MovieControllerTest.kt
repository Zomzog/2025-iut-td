package iut.nantes

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

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
class MovieControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun demoGet() {
       mockMvc.get("/api/movies")
            .andExpect {
                status { isOk() }
                content { contentType("application/json") }
                jsonPath("$[0].name") { value("The Dark Knight") }
            }
    }
}