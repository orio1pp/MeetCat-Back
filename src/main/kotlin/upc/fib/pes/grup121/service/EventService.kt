package upc.fib.pes.grup121.service

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import upc.fib.pes.grup121.dto.EventsDTO
import upc.fib.pes.grup121.exception.EventNotFoundException
import upc.fib.pes.grup121.model.Event
import upc.fib.pes.grup121.model.EventDTO
import upc.fib.pes.grup121.repository.EventRepository
import java.time.LocalDateTime

@Service
class EventService(val repository: EventRepository) {

    fun getPaginated(page: Int, size: Int?): EventsDTO {
        val events = repository.findAll(PageRequest.of(page, size ?: repository.count().toInt()))
        val eventsContent = events.content.map {
            it.toDto()
        }
        return EventsDTO(eventsContent, events.number, events.size)
    }

    fun getById(id: Long):Event{
        return if (repository.existsById(id)) repository.findById(id).get()
        else throw EventNotFoundException("Not found event with id $id")
    }

    fun create(event: EventDTO): Event {
        event.createdDate = LocalDateTime.now()
        event.lastUpdate = event.createdDate
        return repository.save(Event.fromDto(event))
    }

    fun remove(id: Long) {
        if (repository.existsById(id)) repository.deleteById(id)
        else throw EventNotFoundException("Not found event with id $id")
    }

    fun update(id: Long, event: EventDTO): Event {
        return if (repository.existsById(id)) {
            event.lastUpdate = LocalDateTime.now()
            repository.save(Event.fromDto(event, repository.findById(id).get()))
        } else throw EventNotFoundException("Not found event with id $id")
    }
}