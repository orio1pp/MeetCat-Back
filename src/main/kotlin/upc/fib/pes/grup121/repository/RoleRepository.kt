package upc.fib.pes.grup121.repository

import org.springframework.data.jpa.repository.JpaRepository
import upc.fib.pes.grup121.model.Role


interface RoleRepository: JpaRepository<Role, Long> {
    fun findByName(name: String): Role
}