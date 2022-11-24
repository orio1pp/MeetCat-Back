package upc.fib.pes.grup121.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.Repository
import upc.fib.pes.grup121.model.User

interface UserRepository : CrudRepository<User, Long> {
    @Query("select * FROM user u where u.username = :username", nativeQuery = true)
    fun findByUsername(username: String): User

    fun existsByUsername(username: String): Boolean
}