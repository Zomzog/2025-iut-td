package iut.nantes

import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Primary
class ListDatabase : Database {
    private val users = mutableListOf<User>()

    override fun save(user: User) {
        users.add(user)
    }

    override fun delete(user: User) {
        users.removeIf { it.id == user.id }
    }

    override fun update(user: User) {
        val index = users.indexOfFirst { it.id == user.id }
        if (index != -1) {
            users[index] = user
        }
    }

    override fun findOne(id: UUID): User? {
        return users.find { it.id == id }
    }

    override fun findAll(name: String?): List<User> {
        return if (name == null) {
            users.toList()
        } else {
            users.filter { it.name == name }
        }
    }

}