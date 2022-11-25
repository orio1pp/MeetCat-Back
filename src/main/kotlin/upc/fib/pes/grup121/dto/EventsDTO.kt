package upc.fib.pes.grup121.dto

import upc.fib.pes.grup121.model.Event

data class EventsDTO(
        val events: List<Event>,
        val page: Int,
        val size: Int
){
}
