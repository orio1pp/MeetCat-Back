package upc.fib.pes.grup121.service

import org.springframework.stereotype.Service
import upc.fib.pes.grup121.dto.UserDTO
import upc.fib.pes.grup121.exception.IncorrectPasswordException
import upc.fib.pes.grup121.exception.UserNotFoundException
import upc.fib.pes.grup121.model.User
import upc.fib.pes.grup121.repository.UserRepository
import upc.fib.pes.grup121.util.PasswordEncryption
import java.time.LocalDateTime

@Service
class UserService(val repository: UserRepository) {

    fun getAll(): List<User> = repository.findAll()

    fun getById(id: Long): User {
        return if(repository.existsById(id)) repository.findById(id).get()
        else throw UserNotFoundException("User with id $id not found")
    }

    fun create(user: UserDTO) : User {
        user.createdDate = LocalDateTime.now()
        user.lastUpdate = user.createdDate
        return repository.save(User.fromDto(user))
    }

    fun remove(id : Long) {
        if (repository.existsById(id)) repository.deleteById(id)
        else throw UserNotFoundException("User with id $id not found")
    }

    fun update(id : Long, user : UserDTO, password: String) : User {
        return if (repository.existsById(id)) {
            user.lastUpdate = LocalDateTime.now()

            val passwordEncryption = PasswordEncryption()
            val salt = passwordEncryption.generateSalt() + password
            val hash = passwordEncryption.hashString(salt)
            if (hash == repository.findById(id).get().hash) {
                repository.save(User.fromDto(user, repository.findById(id).get()))
            }
            else throw IncorrectPasswordException("Incorrect password")
        }
        else throw UserNotFoundException("User with id $id not found")
    }
}