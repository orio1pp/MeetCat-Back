package upc.fib.pes.grup121.model

import org.hibernate.annotations.DynamicUpdate
import upc.fib.pes.grup121.dto.UserDTO
import upc.fib.pes.grup121.util.PasswordEncryption
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@DynamicUpdate
@Table(name = "users")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long?,
    var username: String,
    @Column(unique = true) var email: String,
    var salt: String?,
    var hash: String?,
    var about: String?,
    var createdDate: LocalDateTime? = null,
    var lastUpdate: LocalDateTime? = null
) {
    fun toDto(): UserDTO = UserDTO(
        id = this.id!!,
        username = this.username,
        email = this.email,
        password = "",
        about = this.about,
        createdDate = this.createdDate,
        lastUpdate = this.lastUpdate
    )

    companion object {
        fun fromDto(dto: UserDTO) : User {
            val passwordEncryption = PasswordEncryption()
            val salt = passwordEncryption.generateSalt()
            val hash = passwordEncryption.hashString(salt + dto.password)
            return User(
                id = dto.id!!,
                username = dto.username,
                email = dto.email,
                salt = salt,
                hash = hash,
                about = dto.about,
                createdDate = dto.createdDate,
                lastUpdate = dto.lastUpdate
            )
        }

        fun fromDto(dto: UserDTO, default: User) : User {
            val passwordEncryption = PasswordEncryption()
            val salt = passwordEncryption.generateSalt()
            val hash = passwordEncryption.hashString(salt + dto.password)
            return User(
                id = default.id!!,
                username = dto.username ?: default.username,
                email = dto.email ?: default.email,
                salt = salt,
                hash = hash,
                about = dto.about ?: default.about,
                createdDate = dto.createdDate ?: default.createdDate,
                lastUpdate = dto.lastUpdate ?: default.lastUpdate
            )
        }
    }
}
