package iut.nantes

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.containsExactlyInAnyOrder
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import java.util.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ListDatabaseTest {
    var database = ListDatabase()
    @BeforeEach
    fun setup() {
        database = ListDatabase()
    }

    @Test
    fun `save should add user to database`() {
        // GIVEN
        val user = user()
        // WHEN
        database.save(user)
        // THEN
        assertEquals(user, database.findOne(user.id))
    }

    @Test
    fun `findAll should filter by name`() {
        // GIVEN
        val user1 = user(UUID.randomUUID(), name = "John")
        val user2 = user(UUID.randomUUID(), name = "Jane")
        val user3 = user(UUID.randomUUID(), name = "John")
        database.save(user1)
        database.save(user2)
        database.save(user3)
        // WHEN
        val result = database.findAll("John")
        // THEN
        assertThat(result.map { it.id }).containsExactly(user1.id, user3.id)
    }


    @Test
    fun `delete one`() {
        // GIVEN
        val user1 = user(UUID.randomUUID(), name = "John")
        val user2 = user(UUID.randomUUID(), name = "Jane")
        val user3 = user(UUID.randomUUID(), name = "John")
        database.save(user1)
        database.save(user2)
        database.save(user3)
        // WHEN
        database.delete(user2)
        // THEN
        val result = database.findAll(null)
        assertThat(result).containsExactlyInAnyOrder(user1, user3)
    }

    @Test
    fun `find existing one`() {
        // GIVEN
        val user1 = user(UUID.randomUUID(), name = "John")
        val user2 = user(UUID.randomUUID(), name = "Jane")
        val user3 = user(UUID.randomUUID(), name = "John")
        database.save(user1)
        database.save(user2)
        database.save(user3)
        // WHEN
        val result = database.findOne(user2.id)
        // THEN
        assertThat(result).isEqualTo(user2)
    }

    @Test
    fun `fail to find one`() {
        // GIVEN
        // WHEN
        val result = database.findOne(UUID.randomUUID())
        // THEN
        assertThat(result).isNull()
    }

    @Test
    fun `update one`() {
        // GIVEN
        val user1 = user(UUID.randomUUID(), name = "John")
        database.save(user1)
        // WHEN
        database.update(user1.copy(age = 1337))
        // THEN
        val result = database.findOne(user1.id)
        assertThat(result).isEqualTo(user(user1.id, name = "John", age = 1337))
    }

    fun user(uuid: UUID = UUID(0, 1), name: String = "John Doe", age: Int = 42) = User(uuid, name, "email@noop.pony", age)

}
