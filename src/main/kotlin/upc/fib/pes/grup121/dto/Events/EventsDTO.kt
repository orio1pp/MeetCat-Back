package upc.fib.pes.grup121.dto.Events

data class EventsDTO(
        val events: List<EventDTO>,
        val page: Int,
        val size: Int
){
}
