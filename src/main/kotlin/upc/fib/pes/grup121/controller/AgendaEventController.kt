package upc.fib.pes.grup121.controller

import org.springframework.web.bind.annotation.*
import upc.fib.pes.grup121.agenda.AgendaEventService

@RequestMapping("/agenda")
@RestController
class AgendaEventController (val service: AgendaEventService){
    @PutMapping("/update")
    fun updateData() {
        service.updateData()
    }
}