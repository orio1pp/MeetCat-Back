package upc.fib.pes.grup121.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import upc.fib.pes.grup121.agenda.AgendaEventService
import upc.fib.pes.grup121.model.Event
import upc.fib.pes.grup121.model.EventDTO
import upc.fib.pes.grup121.service.EventService

@RequestMapping("/agenda")
@RestController
class AgendaEventController (val service: AgendaEventService){
    @PutMapping("/update")
    fun updateData() {
        service.updateData()
    }
}