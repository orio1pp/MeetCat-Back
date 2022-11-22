package upc.fib.pes.grup121.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import upc.fib.pes.grup121.model.User

interface UserRepository : JpaRepository<User, Long> {
    @Query("select * FROM User u where u.username = :username", nativeQuery = true)
    fun findByUsername(username: String): User
}