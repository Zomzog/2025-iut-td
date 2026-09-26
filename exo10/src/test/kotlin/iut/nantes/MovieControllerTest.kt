package iut.nantes

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

/**
 * Tests fournis pour les exos 12 à 18, un `@Nested` par exo.
 * Ils servent d'exemples pour l'exo 21, où vous écrivez vous-même les tests des exos 19 et 20.
 *
 * La base est réinitialisée avant chaque test : les tests ne dépendent pas les uns des autres.
 */
@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MovieControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Nested
    @DisplayName("Exo 12 — HelloController")
    inner class Exo12 {

        @Test
        fun hello() {
            mockMvc.get("/hello")
                .andExpect {
                    status { isOk() }
                    content { string("world") }
                }
        }
    }

    @Nested
    @DisplayName("Exo 13 — POST → 201")
    inner class Exo13 {

        @Test
        fun create() {
            mockMvc.post("/api/movies") {
                contentType = MediaType.APPLICATION_JSON
                content = JURASSIC_PARK
            }.andExpect {
                status { isCreated() }
                jsonPath("$.name") { value("Jurassic Park") }
                jsonPath("$.rating") { value(91) }
            }
        }
    }

    @Nested
    @DisplayName("Exo 14 — POST → 409")
    inner class Exo14 {

        @Test
        fun conflict() {
            mockMvc.post("/api/movies") {
                contentType = MediaType.APPLICATION_JSON
                content = JURASSIC_PARK
            }.andExpect { status { isCreated() } }

            mockMvc.post("/api/movies") {
                contentType = MediaType.APPLICATION_JSON
                content = JURASSIC_PARK
            }.andExpect { status { isConflict() } }
        }

        @Test
        fun `un film existant dans la base de départ est un conflit`() {
            mockMvc.post("/api/movies") {
                contentType = MediaType.APPLICATION_JSON
                content = """{"name": "Inception", "rating": 8, "releaseDate": 2010, "languages": ["VF"]}"""
            }.andExpect { status { isConflict() } }
        }
    }

    @Nested
    @DisplayName("Exo 15 — GET liste")
    inner class Exo15 {

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

    @Nested
    @DisplayName("Exo 16 — GET unitaire")
    inner class Exo16 {

        @Test
        fun found() {
            mockMvc.get("/api/movies/Inception")
                .andExpect {
                    status { isOk() }
                    jsonPath("$.name") { value("Inception") }
                    jsonPath("$.releaseDate") { value(2010) }
                }
        }

        @Test
        fun notFound() {
            mockMvc.get("/api/movies/Dune")
                .andExpect { status { isNotFound() } }
        }
    }

    @Nested
    @DisplayName("Exo 17 — PUT")
    inner class Exo17 {

        @Test
        fun updated() {
            mockMvc.put("/api/movies/Inception") {
                contentType = MediaType.APPLICATION_JSON
                content = """{"name": "Inception", "releaseDate": 2010, "rating": 87, "languages": ["VF", "VO"]}"""
            }.andExpect {
                status { isOk() }
                jsonPath("$.rating") { value(87) }
            }

            mockMvc.get("/api/movies/Inception")
                .andExpect { jsonPath("$.rating") { value(87) } }
        }

        @Test
        fun `nom différent dans le chemin et le body`() {
            mockMvc.put("/api/movies/Inception") {
                contentType = MediaType.APPLICATION_JSON
                content = """{"name": "My Little Pony", "releaseDate": 2010, "rating": 87, "languages": ["VF"]}"""
            }.andExpect { status { isBadRequest() } }
        }

        @Test
        fun notFound() {
            mockMvc.put("/api/movies/Dune") {
                contentType = MediaType.APPLICATION_JSON
                content = """{"name": "Dune", "releaseDate": 2010, "rating": 87, "languages": ["VF"]}"""
            }.andExpect { status { isNotFound() } }
        }
    }

    @Nested
    @DisplayName("Exo 18 — DELETE")
    inner class Exo18 {

        @Test
        fun deleted() {
            mockMvc.delete("/api/movies/Inception")
                .andExpect { status { isNoContent() } }

            mockMvc.get("/api/movies/Inception")
                .andExpect { status { isNotFound() } }
        }

        @Test
        fun notFound() {
            mockMvc.delete("/api/movies/Dune")
                .andExpect { status { isNotFound() } }
        }
    }
}

private const val JURASSIC_PARK =
    """{"name": "Jurassic Park", "rating": 91, "releaseDate": 1993, "languages": ["VO", "VFF", "VFQ"]}"""
