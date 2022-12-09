package upc.fib.pes.grup121.dto

import upc.fib.pes.grup121.model.Event
import upc.fib.pes.grup121.model.EventDTO

data class EventsDTO(
        val events: List<EventDTO>,
        val page: Int,
        val size: Int
){
}
