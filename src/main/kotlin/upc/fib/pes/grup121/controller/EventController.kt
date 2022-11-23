package upc.fib.pes.grup121.controller

import org.springframework.data.repository.query.Param
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import upc.fib.pes.grup121.dto.EventsDTO
import upc.fib.pes.grup121.model.Event
import upc.fib.pes.grup121.model.EventDTO
import upc.fib.pes.grup121.service.EventService

@RequestMapping("/events")
@RestController
class EventController (val service: EventService){
    @GetMapping
    fun getEvents(
            @RequestParam("page", defaultValue = "0") page: Int,
            @RequestParam("size") size: Int?
    ): EventsDTO {
        return service.getPaginated(page, size)
    }

    @GetMapping("/{id}")
    fun getEvent(@PathVariable id: Long) :Event{
        return service.getById(id)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun saveEvent(@RequestBody event: EventDTO) {
        service.create(event)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteEvent(@PathVariable id: Long){
        service.remove(id)
    }

    @PutMapping("/{id}")
    fun updateEvent(
        @PathVariable id: Long, @RequestBody event: EventDTO
    ) {
        service.update(id, event)
    }


//        listOf(
//        Event("1", "Correbars","description"),
//        Event("2", "Concert Rosalia", "description"),
//        Event("3", "Calçotada popular","description"),
//    )
}