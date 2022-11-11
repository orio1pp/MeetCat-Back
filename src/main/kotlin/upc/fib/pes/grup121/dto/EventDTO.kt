package upc.fib.pes.grup121.model

import java.time.LocalDateTime

data class EventDTO(
    var id: Long?,
    var title: String,
    var subtitle: String?,
    var description: String?,
    var initDate: LocalDateTime,
    var endDate: LocalDateTime?,
    var link: String?,
    var placeName: String?,
    var location: String?,
    var address: String?,
    var lastUpdate: LocalDateTime? = null,
    var createdDate: LocalDateTime? = null
){

}