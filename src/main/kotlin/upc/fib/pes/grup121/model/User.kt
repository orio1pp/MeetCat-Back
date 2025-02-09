package upc.fib.pes.grup121.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.annotations.DynamicUpdate
import upc.fib.pes.grup121.dto.User.UserDTO
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
    var lastUpdate: LocalDateTime? = null,
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "attendance",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "event_id", referencedColumnName = "id")]
    )
    @JsonIgnoreProperties("attendees")
    val attendingEvents: MutableList<Event>,
    @ManyToMany(mappedBy = "likedByUserList")
    @JsonIgnoreProperties("eventsLiked")
    val eventsLiked: MutableList<Event>,
    @ManyToMany(mappedBy = "dislikedByUserList")
    @JsonIgnoreProperties("eventsDisliked")
    val eventsDisliked: MutableList<Event>,
) {
    fun toDto(): UserDTO = UserDTO(
        id = this.id,
        username = this.username,
        password = this.password,
        roles = this.roles,
        about = this.about,
        createdDate = this.createdDate,
        lastUpdate = this.lastUpdate,
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
                lastUpdate = dto.lastUpdate,
                attendingEvents = mutableListOf(),
                eventsLiked = mutableListOf(),
                eventsDisliked = mutableListOf(),
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
                lastUpdate = dto.lastUpdate ?: default.lastUpdate,
                attendingEvents = default.attendingEvents,
                eventsLiked = default.eventsLiked,
                eventsDisliked = default.eventsDisliked,
            )
        }
    }
}
