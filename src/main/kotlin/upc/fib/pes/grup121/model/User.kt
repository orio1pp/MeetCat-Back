package upc.fib.pes.grup121.model

import org.hibernate.annotations.DynamicUpdate
import upc.fib.pes.grup121.dto.UserDTO
import upc.fib.pes.grup121.util.PasswordEncryption
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@DynamicUpdate
@Table(name = "user")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long?,
    @Column(unique = true) var username: String,
    var password: String,
    @ManyToMany(fetch = FetchType.EAGER) var roles: MutableCollection<Role> = mutableListOf<Role>(),
    var about: String?,
    var createdDate: LocalDateTime? = null,
    var lastUpdate: LocalDateTime? = null
) {
    fun toDto(): UserDTO = UserDTO(
        id = this.id,
        username = this.username,
        password = this.password,
        roles = this.roles,
        about = this.about,
        createdDate = this.createdDate,
        lastUpdate = this.lastUpdate
    )

    companion object {
        fun fromDto(dto: UserDTO) : User {
            return User(
                id = dto.id,
                username = dto.username,
                password = dto.password,
                roles = dto.roles,
                about = dto.about,
                createdDate = dto.createdDate,
                lastUpdate = dto.lastUpdate
            )
        }

        fun fromDto(dto: UserDTO, default: User) : User {
            return User(
                id = default.id,
                username = dto.username ?: default.username,
                password = dto.password ?: default.password,
                roles = dto.roles ?: default.roles,
                about = dto.about ?: default.about,
                createdDate = dto.createdDate ?: default.createdDate,
                lastUpdate = dto.lastUpdate ?: default.lastUpdate
            )
        }
    }
}
