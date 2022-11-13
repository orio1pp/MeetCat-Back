package upc.fib.pes.grup121.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import upc.fib.pes.grup121.model.Event

@Repository
interface EventRepository : JpaRepository<Event, Long>