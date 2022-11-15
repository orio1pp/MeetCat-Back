package upc.fib.pes.grup121.agenda

import org.springframework.core.ParameterizedTypeReference
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import upc.fib.pes.grup121.dto.AgendaEventDTO
import upc.fib.pes.grup121.repository.EventRepository
import upc.fib.pes.grup121.service.EventService
import java.util.*


@Service
class AgendaEventService(val repository: EventRepository, val eventService: EventService) {

    fun updateData() {
        var agendaEventsList: List<AgendaEventDTO> = getAllAgendaEvents()
        mergeWithDB(agendaEventsList);
    }

    private fun mergeWithDB(agendaEventsList: List<AgendaEventDTO>) {
        agendaEventsList.forEach { event: AgendaEventDTO ->
            println("Merging")
            if(repository.existsByAgendaEventCode(event.codi)) {
                val dbEvent = repository.findByAgendaEventCode(event.codi!!)
                var agendaEvent = event.toEvent()
                if (dbEvent != null && dbEvent.agendaEventCode == event.codi) {
                    agendaEvent.id = dbEvent.id
                    agendaEvent.lastUpdate = dbEvent.lastUpdate
                    agendaEvent.createdDate = dbEvent.createdDate
                    eventService.update(dbEvent.id!!, agendaEvent.toDto())
                }
            }
            else {
                var newEvent = event.toEvent()
                var newewewEvent = newEvent.toDto()
                eventService.create(newewewEvent)
            }
        }
    }

    fun getAllAgendaEvents(): List<AgendaEventDTO> {
        var uri = "https://analisi.transparenciacatalunya.cat/resource/rhpv-yr4f.json"
        var restTemplate = RestTemplate()
        var headers = org.springframework.http.HttpHeaders()
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON))
        val entity = HttpEntity("body", headers)
        val typeRef: ParameterizedTypeReference<List<AgendaEventDTO?>?> =
            object : ParameterizedTypeReference<List<AgendaEventDTO?>?>() {}
        val result = restTemplate.exchange(uri, HttpMethod.GET, entity, typeRef)
        return result.body as List<AgendaEventDTO>
//        println("Result events agenda: " + eventList.size)
    }

}