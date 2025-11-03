package iut.nantes.exo21.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import iut.nantes.exo21.errors.ImATeapotException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(DemoController::class)
class DemoDtoTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockkBean
    lateinit var demoService: DemoService

    @Test
    fun `happy path`() {
        // GIVEN
        every { demoService.list() } throws ImATeapotException()
        // WHEN
        mockMvc.get("/api/v1/demo")
            // THEN
            .andExpect {
                status { isBadRequest() }
            }
    }
}