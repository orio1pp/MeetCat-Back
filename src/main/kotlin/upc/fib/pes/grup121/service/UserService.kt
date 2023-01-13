package upc.fib.pes.grup121.service

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import upc.fib.pes.grup121.dto.User.UserDTO
import upc.fib.pes.grup121.exception.UserNotFoundException
import upc.fib.pes.grup121.model.Event
import upc.fib.pes.grup121.model.Role
import upc.fib.pes.grup121.model.User
import upc.fib.pes.grup121.repository.UserRepository
import java.lang.Exception
import java.time.LocalDateTime
import java.util.*

@Service
class UserService(
        val userRepository: UserRepository,
) : UserDetailsService {

    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    fun getAll(): List<User> = userRepository.findAll().toList()

    fun getById(id: Long): User {
        return if(userRepository.existsById(id)) userRepository.findById(id).get()
        else throw UserNotFoundException("User with id $id not found")
    }

    fun existsByUsername(username: String): Boolean {
        if (userRepository.existsByUsername(username)) return true
        return false
    }

    fun getByUsername(username: String): UserDTO {
        return if (userRepository.existsByUsername(username)) userRepository.findByUsername(username).toDto()
        else throw UserNotFoundException("User with id $username not found")
    }

    fun getAgendaUser(): User {
        if (userRepository.existsByUsername("Agenda Cultural"))
            return userRepository.findByUsername("Agenda Cultural")

        val user = User(
            id = null,
            username = "Agenda Cultural",
            password = "",
            roles = mutableListOf(),
            about = "",
            createdDate = LocalDateTime.now(),
            lastUpdate = LocalDateTime.now(),
            attendingEvents = mutableListOf(),
            eventsLiked = mutableListOf(),
            eventsDisliked = mutableListOf(),
        )

        userRepository.save(user)

        return user
    }

    fun create(user: UserDTO) : User {
        user.createdDate = LocalDateTime.now()
        user.lastUpdate = user.createdDate
        user.password = passwordEncoder().encode(user.password)
        return if (!userRepository.existsByUsername(user.username)) {
            userRepository.save(User.fromDto(user))
        }
        else throw Exception("User already exists")
    }

    fun remove(id : Long): Optional<User> {
        if (userRepository.existsById(id)) {
            val user = userRepository.findById(id)
            userRepository.deleteById(id)
            return user
        }
        else throw UserNotFoundException("User with id $id not found")
    }

    fun update(id : Long, user : UserDTO) : UserDTO {
        return if (userRepository.existsById(id)) {
            user.lastUpdate = LocalDateTime.now()
            user.password = passwordEncoder().encode(user.password)
            userRepository.save(User.fromDto(user, userRepository.findById(id).get())).toDto()
        }
        else throw UserNotFoundException("User with id $id not found")
    }

    override fun loadUserByUsername(username: String): UserDetails {
        var user: User = userRepository.findByUsername(username)
        if (user == null) {
            throw UsernameNotFoundException("User not found in the database.")
        }
        var authorities: MutableCollection<SimpleGrantedAuthority> = mutableListOf()
        for (role : Role in user.roles) {
            authorities.add(SimpleGrantedAuthority(role.name))
        }
        return org.springframework.security.core.userdetails.User(user.username, user.password, authorities)
    }
}