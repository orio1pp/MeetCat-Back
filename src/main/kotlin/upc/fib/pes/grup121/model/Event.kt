package upc.fib.pes.grup121.model

import org.hibernate.annotations.DynamicUpdate
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
    @Column(columnDefinition="TEXT") var description: String?,
    var initDate: LocalDateTime,
    var endDate: LocalDateTime?,
    var link: String?,
    var placeName: String?,
    var latitud: Double,
    var longitud: Double,
    var address: String?,
    var lastUpdate: LocalDateTime? = null,
    var createdDate: LocalDateTime? = null,
    var agendaEventCode: Long?
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
        location = this.latitud.toString()+','+this.longitud.toString(),
        address = this.address,
        agendaEventCode = this.agendaEventCode
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
            latitud = dto.location!!.split(",")[0].toDouble(),
            longitud = dto.location!!.split(",")[1].toDouble(),
            address = dto.address,
            agendaEventCode = dto.agendaEventCode

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
            latitud = if(dto.location != null) dto.location!!.split(",")[1].toDouble() else default.latitud,
            longitud =  if(dto.location != null) dto.location!!.split(",")[1].toDouble() else default.longitud,
            address = dto.address ?: default.address,
            agendaEventCode = dto.agendaEventCode ?: default.agendaEventCode,
        )

    }

}