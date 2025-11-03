package iut.nantes

import org.springframework.beans.factory.annotation.Autowired

class SuperUserService {

    @Autowired
    lateinit var database: Database

    fun findAll(): List<User> {
        return database.findAll(null)
    }
}