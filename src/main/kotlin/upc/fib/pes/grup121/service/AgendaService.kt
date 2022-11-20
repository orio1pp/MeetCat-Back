package upc.fib.pes.grup121.agenda

import org.springframework.core.ParameterizedTypeReference
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import upc.fib.pes.grup121.dto.AgendaEventDTO
import upc.fib.pes.grup121.model.Event
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
        //get events from DB
        var dbEvents: MutableList<Event> = repository.findAll().toMutableList()
        //save codes in DB
        var missingCodes: MutableList<Long> = getAgendaCodes(dbEvents)

        agendaEventsList.forEach { event: AgendaEventDTO ->
            println("Merging")
            if(repository.existsByAgendaEventCode(event.codi)) {
                val dbEvent = dbEvents.find { it.agendaEventCode!! == event.codi!! }
                var agendaEvent = event.toEvent()
                if (dbEvent != null && dbEvent.agendaEventCode == event.codi) {
                    agendaEvent.id = dbEvent.id
                    agendaEvent.lastUpdate = dbEvent.lastUpdate
                    agendaEvent.createdDate = dbEvent.createdDate
                    //we remove the found codes on missingCodes
                    missingCodes.remove(dbEvent.agendaEventCode)
                    dbEvents.remove(dbEvent)
                    dbEvents.add(agendaEvent)
                }
            }
            else {
                var newEvent = event.toEvent()
                dbEvents.add(newEvent)
            }
        }
        //we delete the evens with codes missing in last agenda events
        dbEvents = deleteByAgendaCodes(dbEvents, missingCodes)
        eventService.saveAll(dbEvents)
    }

    private fun getAgendaCodes(events: List<Event>): MutableList<Long> {
        val agendaCodes: MutableList<Long> = mutableListOf()
        events.forEach {
            if(it.agendaEventCode!=null){
                agendaCodes.add(it.agendaEventCode!!)
            }
        }
        return agendaCodes
    }

    private fun deleteByAgendaCodes(events: MutableList<Event>, missingCodes: MutableList<Long>): MutableList<Event>{
        missingCodes.forEach {
            var code=it
            val event = events.find{
                it.agendaEventCode == code
            }
            repository.deleteById(event!!.id!!)
            events.remove(event)
        }
        return events
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