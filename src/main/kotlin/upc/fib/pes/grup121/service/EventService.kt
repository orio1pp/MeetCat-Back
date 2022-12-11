package upc.fib.pes.grup121.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import upc.fib.pes.grup121.dto.Events.EventDTO
import upc.fib.pes.grup121.dto.Events.EventsDTO
import upc.fib.pes.grup121.exception.EventNotFoundException
import upc.fib.pes.grup121.model.Event
import upc.fib.pes.grup121.repository.EventRepository
import java.time.LocalDateTime

@Service
class EventService(val repository: EventRepository) {

    fun getPaginated(page: Int, size: Int?, title: String?): EventsDTO {

        val events: Page<Event> = if (title != null) {
            repository.findByTitleContaining(title, PageRequest.of(page, size ?: repository.count().toInt()))
        } else {
            repository.findAll(PageRequest.of(page, size ?: repository.count().toInt()))
        }

        val eventsContent = events.content.map {
            it.toDto()
        }

        return EventsDTO(eventsContent, events.number, events.size)
    }

    fun getById(id: Long):Event{
        return if (repository.existsById(id)) repository.findById(id).get()
        else throw EventNotFoundException("Not found event with id $id")
    }

    fun getByDistance(latitud: Double, longitud: Double, distance: Double, page: Int, size: Int?): EventsDTO{
        var response = repository.findByDistance(latitud, longitud, distance, page, if(size ==null) 1000 else size!!)
        return EventsDTO(response.map { it.toDto() }, page, response.size)
    }

    fun create(event: EventDTO): Event {
        event.createdDate = LocalDateTime.now()
        event.lastUpdate = event.createdDate
        event.attendeesCount = 0
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