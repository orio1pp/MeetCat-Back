package upc.fib.pes.grup121.controller

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import upc.fib.pes.grup121.dto.User.UserDTO
import upc.fib.pes.grup121.model.Role
import upc.fib.pes.grup121.model.User
import upc.fib.pes.grup121.service.UserService
import java.net.URI
import java.util.*
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

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

    @GetMapping("/name")
    fun getUser(username: String): ResponseEntity<UserDTO> {
        return ResponseEntity.ok().body(service.getByUsername(username))
    }

    @GetMapping("/me")
    fun getUser(): ResponseEntity<UserDTO> {
        val username = SecurityContextHolder.getContext().authentication.name
        return ResponseEntity.ok().body(service.getByUsername(username))
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun saveUser(@RequestBody user: UserDTO): ResponseEntity<User> {
        val uri: URI = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user").toUriString())
        return ResponseEntity.created(uri).body(service.create(user))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Optional<User>> {
        val user = service.remove(id)
        return ResponseEntity.ok().body(user)
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody user: UserDTO): ResponseEntity<UserDTO> {
        return ResponseEntity.ok().body(service.update(id, user))
    }

    @GetMapping("/refresh/token")
    fun refreshToken(request: HttpServletRequest, response: HttpServletResponse) {
        var authorizationHeader: String = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                var refresh_token: String = authorizationHeader.substring("Bearer ".length)
                var algorithm: Algorithm = Algorithm.HMAC256("secret".toByteArray())
                var verifier: JWTVerifier = JWT.require(algorithm).build()
                var decodedJWT: DecodedJWT = verifier.verify(refresh_token)
                var username: String = decodedJWT.subject
                var user : User = User.fromDto(service.getByUsername(username))

                var access_token: String =
                    JWT.create().withSubject(user.username).withExpiresAt(Date(System.currentTimeMillis() + 30 * 60 * 1000))
                        .withIssuer(request?.requestURL.toString())
                        .withClaim("roles", user.roles.stream().map(Role::name).collect(
                            Collectors.toList()))
                        .sign(algorithm)

                var tokens : MutableMap<String, String> = mutableMapOf()
                tokens.put("access_token", access_token)
                tokens.put("refresh_token", refresh_token)
                response.contentType = APPLICATION_JSON_VALUE
                ObjectMapper().writeValue(response.outputStream, tokens)

            } catch (ex: Exception) {
                response.setHeader("error", ex.message)
                response.status = 403
                var error : MutableMap<String, String> = mutableMapOf()
                ex.message?.let { error.put("error", it) }
                response.contentType = APPLICATION_JSON_VALUE
                ObjectMapper().writeValue(response.outputStream, error)
            }
        }
        else {
            throw RuntimeException("refresh token is missing")
        }
    }
}