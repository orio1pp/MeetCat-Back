package upc.fib.pes.grup121.controller

import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import upc.fib.pes.grup121.dto.UserDTO
import upc.fib.pes.grup121.model.User
import upc.fib.pes.grup121.service.UserService
import java.net.URI

@RequestMapping("/users")
@RestController
class UserController(val service: UserService) {

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @GetMapping
    fun getUsers(): ResponseEntity<List<User>> {
        return ResponseEntity.ok().body(service.getAll())
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long): ResponseEntity<User> {
        return ResponseEntity.ok().body(service.getById(id))
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun saveUser(@RequestBody user: UserDTO): ResponseEntity<User> {
        val uri: URI = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user").toUriString())
        return ResponseEntity.created(uri).body(service.create(user))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(@PathVariable id: Long) {
        service.remove(id)
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody user: UserDTO): ResponseEntity<User> {
        return ResponseEntity.ok().body(service.update(id, user))
    }
}