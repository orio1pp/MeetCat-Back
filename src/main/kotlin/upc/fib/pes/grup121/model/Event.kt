package upc.fib.pes.grup121.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.annotations.DynamicUpdate
import upc.fib.pes.grup121.dto.Events.EventDTO
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@DynamicUpdate
@Table(name = "events")
data class Event(
    @Id  @GeneratedValue(strategy = GenerationType.AUTO) var id: Long?,
    var title: String,
    var subtitle: String?,
    @Column(columnDefinition="TEXT") var description: String?,
    var initDate: LocalDateTime,
    var endDate: LocalDateTime?,
    var link: String?,
    var placeName: String?,
    var location: String?,
    var address: String?,
    var lastUpdate: LocalDateTime? = null,
    var createdDate: LocalDateTime? = null,
    var agendaEventCode: Long?,
    @ManyToMany(mappedBy = "attendingEvents")
    @JsonIgnoreProperties("attendingEvents")
    var attendees: MutableList<User>,
){
    fun toDto(): EventDTO = EventDTO(
        id = this.id,
        title = this.title,
        description = this.description,
        lastUpdate = this.lastUpdate,
        createdDate = this.createdDate,
        subtitle = this.subtitle,
        initDate = this.initDate,
        endDate = this.endDate,
        link = this.link,
        placeName = this.placeName,
        location = this.location,
        address = this.address,
        agendaEventCode = this.agendaEventCode,
    )


    companion object {

        fun fromDto(dto: EventDTO) = Event(
            id = dto.id,
            title = dto.title,
            description = dto.description,
            lastUpdate = dto.lastUpdate,
            createdDate = dto.createdDate,
            subtitle = dto.subtitle,
            initDate = dto.initDate,
            endDate = dto.endDate,
            link = dto.link,
            placeName = dto.placeName,
            location = dto.location,
            address = dto.address,
            agendaEventCode = dto.agendaEventCode,
            attendees = mutableListOf(),

  )

        fun fromDto(dto: EventDTO, default: Event) = Event(
            id = default.id!!,
            title = dto.title  ?: default.title,
            description = dto.description ?: default.description,
            lastUpdate = dto.lastUpdate  ?: default.lastUpdate,
            createdDate = dto.createdDate  ?: default.createdDate,
            subtitle = dto.subtitle ?: default.subtitle,
            initDate = dto.initDate ?: default.initDate,
            endDate = dto.endDate ?: default.endDate,
            link = dto.link ?: default.link,
            placeName = dto.placeName ?: default.placeName,
            location = dto.location ?: default.location,
            address = dto.address ?: default.address,
            agendaEventCode = dto.agendaEventCode ?: default.agendaEventCode,
            attendees = mutableListOf(),
        )

    }

}