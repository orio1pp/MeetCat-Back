package upc.fib.pes.grup121.model

import org.hibernate.annotations.DynamicUpdate
import upc.fib.pes.grup121.dto.UserDTO
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@DynamicUpdate
@Table(name = "users")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long?,
    var username: String,
    @Column(unique = true) var email: String,
    var hash: String?,
    var salt: String?,
    var about: String?,
    var createdDate: LocalDateTime? = null,
    var lastUpdate: LocalDateTime? = null
) {
    fun toDto(): UserDTO = UserDTO(
        id = this.id!!,
        username = this.username,
        email = this.email,
        hash = this.hash!!,
        salt = this.salt!!,
        about = this.about,
        createdDate = this.createdDate,
        lastUpdate = this.lastUpdate
    )

    companion object {
        fun fromDto(dto: UserDTO) = User(
            id = dto.id!!,
            username = dto.username,
            email = dto.email,
            hash = dto.hash,
            salt = dto.salt,
            about = dto.about,
            createdDate = dto.createdDate,
            lastUpdate = dto.lastUpdate
        )

        fun fromDto(dto: UserDTO, default: User) = User(
            id = default.id!!,
            username = dto.username ?: default.username,
            email = dto.email ?: default.email,
            hash = dto.hash ?: default.hash,
            salt = dto.salt ?: default.salt,
            about = dto.about ?: default.about,
            createdDate = dto.createdDate ?: default.createdDate,
            lastUpdate = dto.lastUpdate ?: default.lastUpdate
        )
    }
}
