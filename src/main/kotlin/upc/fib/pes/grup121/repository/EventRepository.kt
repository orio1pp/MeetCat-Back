package upc.fib.pes.grup121.repository

import org.springframework.data.geo.Distance
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

    @Query(
        nativeQuery = true,
        value="SELECT * FROM events e WHERE ( 6371 * acos( cos( radians(?1) ) * cos( radians( e.latitud ) )" +
                " * cos( radians( e.longitud ) - radians(?2) ) + sin( radians(?1) ) * sin( radians( e.latitud ) ) ) )" +
                " <= ?3 LIMIT ?4 , ?5")
    fun findByDistance(latitud : Double, longitud : Double, distance : Double, limit1:Int, limit2:Int): List<Event>
}