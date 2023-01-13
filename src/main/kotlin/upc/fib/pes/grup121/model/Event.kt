package upc.fib.pes.grup121.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.annotations.DynamicUpdate
import upc.fib.pes.grup121.dto.Events.EventDTO
import org.springframework.data.geo.Point
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@DynamicUpdate
@Table(name = "events")
data class Event(
    @Id  @GeneratedValue(strategy = GenerationType.AUTO) var id: Long?,
    var title: String,
    var subtitle: String?,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdByUsername")
    var user: User,
    @Column(columnDefinition="TEXT") var description: String?,
    var initDate: LocalDateTime,
    var endDate: LocalDateTime?,
    var link: String?,
    var placeName: String?,
    var latitud: Double?,
    var longitud: Double?,
    var address: String?,
    var lastUpdate: LocalDateTime? = null,
    var createdDate: LocalDateTime? = null,
    var agendaEventCode: Long?,
    @ManyToMany(mappedBy = "attendingEvents")
    @JsonIgnoreProperties("attendingEvents")
    var attendees: MutableList<User>,
    var attendeesCount: Int,
    var reported: Boolean? = false,
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "likes",
            joinColumns = [JoinColumn(name = "event_id", referencedColumnName = "id")],
            inverseJoinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")]
    )
    @JsonIgnoreProperties("eventsLiked")
    var likedByUserList: MutableList<User>,
    var likes: Int,
    @JoinTable(
            name = "dislikes",
            joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
            inverseJoinColumns = [JoinColumn(name = "event_id", referencedColumnName = "id")]
    )
    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("eventsDisliked")
    var dislikedByUserList: MutableList<User>,
    var dislikes: Int,
){
    fun toDto(): EventDTO = EventDTO(
        id = this.id,
        title = this.title,
        username = this.user.username,
        description = this.description,
        lastUpdate = this.lastUpdate,
        createdDate = this.createdDate,
        subtitle = this.subtitle,
        initDate = this.initDate,
        endDate = this.endDate,
        link = this.link,
        placeName = this.placeName,
        location = this.latitud.toString()+','+this.longitud.toString(),
        address = this.address,
        agendaEventCode = this.agendaEventCode,
        attendeesCount = this.attendeesCount,
        likes = this.likes,
        dislikes = this.dislikes,
    )


    companion object {

        fun fromDto(dto: EventDTO, user: User) = Event(
            id = dto.id,
            title = dto.title,
            user = user,
            description = dto.description,
            lastUpdate = dto.lastUpdate,
            createdDate = dto.createdDate,
            subtitle = dto.subtitle,
            initDate = dto.initDate,
            endDate = dto.endDate,
            link = dto.link,
            placeName = dto.placeName,
            latitud = dto.location!!.split(",")[0].toDouble(),
            longitud = dto.location!!.split(",")[1].toDouble(),
            address = dto.address,
            agendaEventCode = dto.agendaEventCode,
            attendees = mutableListOf(),
            attendeesCount = dto.attendeesCount,
            likedByUserList = mutableListOf(),
            dislikedByUserList = mutableListOf(),
            reported = false,
            likes = dto.likes,
            dislikes = dto.dislikes,
  )

        fun fromDto(dto: EventDTO, default: Event) = Event(
            id = default.id!!,
            title = dto.title  ?: default.title,
            user = default.user,
            description = dto.description ?: default.description,
            lastUpdate = dto.lastUpdate  ?: default.lastUpdate,
            createdDate = dto.createdDate  ?: default.createdDate,
            subtitle = dto.subtitle ?: default.subtitle,
            initDate = dto.initDate ?: default.initDate,
            endDate = dto.endDate ?: default.endDate,
            link = dto.link ?: default.link,
            placeName = dto.placeName ?: default.placeName,
            latitud = if(dto.location != null) dto.location!!.split(",")[1].toDouble() else default.latitud,
            longitud =  if(dto.location != null) dto.location!!.split(",")[1].toDouble() else default.longitud,
            address = dto.address ?: default.address,
            agendaEventCode = dto.agendaEventCode ?: default.agendaEventCode,
            attendees = mutableListOf(),
            attendeesCount = dto.attendeesCount,
            reported = false,
            likedByUserList = default.likedByUserList,
            dislikedByUserList = default.dislikedByUserList,
            likes = dto.likes,
            dislikes = dto.dislikes,
        )

    }

}
