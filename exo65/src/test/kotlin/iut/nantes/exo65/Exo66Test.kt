package iut.nantes.exo65

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
class Exo66Test {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun exo66() {
        mockMvc.get("/actuator/info")
            .andExpect { status { isOk() } }
    }

    @Test
    fun exo67() {
        mockMvc.get("/actuator/info")
            .andExpect { status { isOk() } }
            .andExpect { jsonPath("$.app-name") { value("exo65") }}
    }
}