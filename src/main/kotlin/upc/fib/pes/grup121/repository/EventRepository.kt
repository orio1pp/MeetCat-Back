package upc.fib.pes.grup121.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import upc.fib.pes.grup121.model.Event

@Repository
interface EventRepository : CrudRepository<Event, Long>, PagingAndSortingRepository<Event, Long> {

    fun existsByAgendaEventCode(agendaEventCode: Long?): Boolean

    fun findByAgendaEventCode(agendaEventCode: Long?): Event

    fun findByTitleContaining(title: String, pageable: Pageable): Page<Event>

    fun findByReportedIsTrue(pageable: Pageable): Page<Event>

    fun findByReportedIsTrueAnAndTitleEquals(title: String, pageable: Pageable): Page<Event>
}