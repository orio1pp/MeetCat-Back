package upc.fib.pes.grup121.service

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import upc.fib.pes.grup121.dto.UserDTO
import upc.fib.pes.grup121.exception.IncorrectPasswordException
import upc.fib.pes.grup121.exception.UserNotFoundException
import upc.fib.pes.grup121.model.User
import upc.fib.pes.grup121.repository.RoleRepository
import upc.fib.pes.grup121.repository.UserRepository
import upc.fib.pes.grup121.util.PasswordEncryption
import java.time.LocalDateTime

@Service
class UserService(val userRepository: UserRepository) {

    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    fun getAll(): List<User> = userRepository.findAll()

    fun getById(id: Long): User {
        return if(userRepository.existsById(id)) userRepository.findById(id).get()
        else throw UserNotFoundException("User with id $id not found")
    }

    fun create(user: UserDTO) : User {
        user.createdDate = LocalDateTime.now()
        user.lastUpdate = user.createdDate
        user.password = passwordEncoder().encode(user.password)
        return userRepository.save(User.fromDto(user))
    }

    fun remove(id : Long) {
        if (userRepository.existsById(id)) userRepository.deleteById(id)
        else throw UserNotFoundException("User with id $id not found")
    }

    fun update(id : Long, user : UserDTO) : User {
        return if (userRepository.existsById(id)) {
            user.lastUpdate = LocalDateTime.now()
            userRepository.save(User.fromDto(user, userRepository.findById(id).get()))
        }
        else throw UserNotFoundException("User with id $id not found")
    }
}