package iut.nantes

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import java.util.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ExercicesTest {

    @Nested
    inner class Exo1 {

        @Test
        fun test() {
            val userService: UserService = TODO()
            userService.save(user())
            val user = userService.findOne(user().id)

            assertThat(user).isEqualTo(user())
        }
    }

    @Nested
    inner class Exo2 {

        @Test
        fun test() {
            val userService: UserService = TODO()
            val superUserService: SuperUserService = TODO()
            userService.save(user())

            assertThat(superUserService.findAll()).isEqualTo(listOf(user()))
        }
    }

    @Nested
    inner class Exo3 {

        @Test
        fun test() {
            val userService: UserService = TODO()
            val superUserService: SuperUserService = TODO()
            userService.save(user())

            assertThat(superUserService.findAll()).isEmpty()
        }
    }
}

private fun user(uuid: UUID = UUID(0, 1)) = User(uuid, "John Doe", "email@noop.pony", 42)
