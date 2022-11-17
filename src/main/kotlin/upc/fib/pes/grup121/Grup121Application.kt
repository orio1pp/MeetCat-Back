package upc.fib.pes.grup121

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import upc.fib.pes.grup121.model.Role
import upc.fib.pes.grup121.model.User
import upc.fib.pes.grup121.model.UserService
import java.util.ArrayList

@SpringBootApplication
class Grup121Application

fun main(args: Array<String>) {
	runApplication<Grup121Application>(*args)
}