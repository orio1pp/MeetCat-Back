package upc.fib.pes.grup121.repository

import org.springframework.data.jpa.repository.JpaRepository
import upc.fib.pes.grup121.model.User

interface UserRepository : JpaRepository<User, Long>