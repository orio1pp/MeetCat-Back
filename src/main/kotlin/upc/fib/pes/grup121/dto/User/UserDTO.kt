package upc.fib.pes.grup121.dto.User

import upc.fib.pes.grup121.model.EventDTO
import upc.fib.pes.grup121.model.Role
import java.time.LocalDateTime

data class UserDTO(
    var id: Long?,
    var username: String,
    var password: String,
    var roles: MutableCollection<Role> = mutableListOf<Role>(),
    var about: String?,
    var lastUpdate: LocalDateTime? = null,
    var createdDate: LocalDateTime? = null,
)
