package iut.nantes

import java.util.UUID

class HashDatabase: Database {
    private val users = mutableMapOf<UUID, User>()

    override fun save(user: User) {
        users[user.id] = user
    }

    override fun delete(user: User) {
        users.remove(user.id)
    }

    override fun update(user: User) {
        users[user.id] = user
    }

    override fun findOne(id: UUID): User? {
        return users[id]
    }

    override fun findAll(name: String?): List<User> {
        return if (name == null) {
            users.values.toList()
        } else {
            users.values.filter { it.name == name }
        }
    }
}