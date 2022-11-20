package upc.fib.pes.grup121.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import upc.fib.pes.grup121.model.Event
import java.awt.print.Pageable

@Repository
interface EventRepository : CrudRepository<Event, Long>, PagingAndSortingRepository<Event, Long> {

    fun existsByAgendaEventCode(agendaEventCode: Long?): Boolean

    fun findByAgendaEventCode(agendaEventCode: Long?): Event
}