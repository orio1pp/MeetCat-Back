package upc.fib.pes.grup121.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import upc.fib.pes.grup121.dto.Attendance.AttendanceDTO
import upc.fib.pes.grup121.dto.Events.EventsDTO
import upc.fib.pes.grup121.model.Event
import upc.fib.pes.grup121.dto.Events.EventDTO
import upc.fib.pes.grup121.service.EventService

@RequestMapping("/events")
@RestController
class EventController (val service: EventService){
    @GetMapping
    fun getEvents(
            @RequestParam("page", defaultValue = "0") page: Int,
            @RequestParam("size") size: Int?,
            @RequestParam("title") title: String?,
            @RequestParam("username") username: String?,
    ): EventsDTO {
        return service.getPaginated(page, size, title, username)
    }
    @GetMapping("/nearest")
    fun getEventsByDistance(
            @RequestParam("page", defaultValue = "0") page: Int,
            @RequestParam("size") size: Int?,
            @RequestParam("latitude", defaultValue = "41.386575") latitude: Double,
            @RequestParam("longitude", defaultValue = "2.170068") longitude: Double,
            @RequestParam("distance", defaultValue = "0.5") distance: Double
    ): EventsDTO {
        //distance is a double in km
        return service.getByDistance(latitude, longitude, distance, page, size)
    }

    @GetMapping("/{id}")
    fun getEvent(@PathVariable id: Long) :Event{
        return service.getById(id)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun saveEvent(@RequestBody event: EventDTO) {
        val username = SecurityContextHolder.getContext().authentication.name
        service.create(username, event)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteEvent(@PathVariable id: Long){
        val username = SecurityContextHolder.getContext().authentication.name
        service.remove(username, id)
    }

    @PutMapping("/{id}")
    fun updateEvent(
        @PathVariable id: Long, @RequestBody event: EventDTO
    ) {
        val username = SecurityContextHolder.getContext().authentication.name
        service.update(username, id, event)
    }

    @PutMapping("/{id}/report")
    fun reportEvent(
        @PathVariable id: Long
    ) {
        service.report(id, true)
    }

    @PutMapping("/{id}/unreport") //NOMES PER ADMINS TODO
    fun unreportEvent(
        @PathVariable id: Long
    ) {
        service.report(id, false)
    }

    @GetMapping("/reported") //NOMES PER ADMINS TODO
    fun getReportedEvents(
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size") size: Int?,
        @RequestParam("title") title: String?,
    ): EventsDTO  {
        return service.getReported(page, size, title)
    }


//        listOf(
//        Event("1", "Correbars","description"),
//        Event("2", "Concert Rosalia", "description"),
//        Event("3", "Calçotada popular","description"),
//    )
}