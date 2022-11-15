package upc.fib.pes.grup121.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import upc.fib.pes.grup121.dto.UserDTO
import upc.fib.pes.grup121.model.User
import upc.fib.pes.grup121.service.UserService

@RequestMapping("/users")
@RestController
class UserController(val service : UserService) {

    @GetMapping
    fun getUsers(): List<User> {
        return service.getAll()
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long) : User {
        return service.getById(id)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun saveUser(@RequestBody user: UserDTO) {
        service.create(user)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(@PathVariable id: Long){
        service.remove(id)
    }

    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: Long, @RequestBody user: UserDTO
    ) {
        service.update(id, user)
    }
}