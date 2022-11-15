package upc.fib.pes.grup121.dto

import java.time.LocalDateTime

data class UserDTO(
    var id: Long?,
    var username: String,
    var email: String,
    var password: String,
    var about: String?,
    var lastUpdate: LocalDateTime? = null,
    var createdDate: LocalDateTime? = null
) {

}
