package upc.fib.pes.grup121.agenda

import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import upc.fib.pes.grup121.dto.Events.AgendaEventDTO
import upc.fib.pes.grup121.repository.EventRepository
import upc.fib.pes.grup121.service.EventService
import upc.fib.pes.grup121.service.UserService
import java.util.*


@Service
class AgendaEventService(val repository: EventRepository, val eventService: EventService, val userService: UserService) {

    fun updateData() {
        var agendaEventsList: List<AgendaEventDTO> = getAllAgendaEvents()
        mergeWithDB(agendaEventsList);
    }

    private fun mergeWithDB(agendaEventsList: List<AgendaEventDTO>) {
        agendaEventsList.forEach { event: AgendaEventDTO ->
            println("Merging")
            val user = userService.getAgendaUser()
            if(repository.existsByAgendaEventCode(event.codi)) {
                val dbEvent = repository.findByAgendaEventCode(event.codi!!)
                var agendaEvent = event.toEvent(user)
                if (dbEvent.agendaEventCode == event.codi) {
                    agendaEvent.id = dbEvent.id
                    agendaEvent.lastUpdate = dbEvent.lastUpdate
                    agendaEvent.createdDate = dbEvent.createdDate
                    agendaEvent.attendeesCount = dbEvent.attendeesCount
                    val agendaEventDto = agendaEvent.toDto()
                    agendaEventDto.attendeesCount = dbEvent.attendeesCount
                    eventService.update(username = "Agenda Cultural", id = dbEvent.id!!, event = agendaEventDto)
                }
            }
            else {
                var newEvent = event.toEvent(user)
                var newEventDto = newEvent.toDto()
                eventService.create(user.username, newEventDto)
            }
        }
    }

    fun getAllAgendaEvents(): List<AgendaEventDTO> {
        var uri = "https://analisi.transparenciacatalunya.cat/resource/rhpv-yr4f.json"
        var restTemplate = RestTemplate()
        var headers = org.springframework.http.HttpHeaders()
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON))
        headers.add("X-App-Token", "VDBxznSGxvsPzf6jAg2ZV8Ph2")
        val entity = HttpEntity("body", headers)
        val typeRef: ParameterizedTypeReference<List<AgendaEventDTO?>?> =
            object : ParameterizedTypeReference<List<AgendaEventDTO?>?>() {}
        val result = restTemplate.exchange(uri, HttpMethod.GET, entity, typeRef)
        return result.body as List<AgendaEventDTO>
    }

}