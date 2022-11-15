package upc.fib.pes.grup121.dto

import java.time.LocalDateTime

data class UserDTO(
    var id: Long?,
    var username: String,
    var email: String,
    var hash: String?,
    var salt: String?,
    var about: String?,
    var lastUpdate: LocalDateTime? = null,
    var createdDate: LocalDateTime? = null
) {

}
