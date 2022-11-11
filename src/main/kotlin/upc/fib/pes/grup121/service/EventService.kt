package upc.fib.pes.grup121.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import upc.fib.pes.grup121.model.Event
import upc.fib.pes.grup121.model.EventDTO
import upc.fib.pes.grup121.repository.EventRepository
import java.time.LocalDateTime

@Service
class EventService(val repository: EventRepository) {

    fun getAll(): List<Event> = repository.findAll()

    fun getById(id: Long): Event = repository.findById(id).get()

    fun create(event: EventDTO): Event {
        event.createdDate = LocalDateTime.now()
        event.lastUpdate = event.createdDate
        return repository.save(Event.fromDto(event))
    }

    fun remove(id: Long) {
        if (repository.existsById(id)) repository.deleteById(id)
        else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    fun update(id: Long, event: EventDTO): Event {
        return if (repository.existsById(id)) {
            event.lastUpdate = LocalDateTime.now()
            repository.save(Event.fromDto(event, repository.findById(id).get()))
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }
}