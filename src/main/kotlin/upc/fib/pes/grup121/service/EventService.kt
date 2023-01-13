package upc.fib.pes.grup121.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import upc.fib.pes.grup121.dto.Events.EventDTO
import upc.fib.pes.grup121.dto.Events.EventsDTO
import upc.fib.pes.grup121.exception.EventNotFoundException
import upc.fib.pes.grup121.exception.UserNotFoundException
import upc.fib.pes.grup121.model.Event
import upc.fib.pes.grup121.model.User
import upc.fib.pes.grup121.repository.EventRepository
import java.time.LocalDateTime
import java.util.stream.IntStream.range

@Service
class EventService(
    val repository: EventRepository,
    val userService: UserService,
    val attendanceService: AttendanceService,
    val likeService: LikeService,
) {


    fun getPaginated(page: Int, size: Int?, title: String?, username: String?): EventsDTO {

        val events: Page<Event> = if (title != null) {
            repository.findByTitleContaining(title, PageRequest.of(page, size ?: repository.count().toInt()))
        } else if (username != null) {
            val user = userService.getByUsername(username)
            repository.findByUser(User.fromDto(user), PageRequest.of(page, size ?: repository.count().toInt()))
        } else {
            repository.findAll(PageRequest.of(page, size ?: repository.count().toInt()))
        }

        val eventsContent = events.content.map {
            it.toDto()
        }

        return EventsDTO(eventsContent, events.number, events.size)
    }

    fun getById(id: Long): Event {
        return if (repository.existsById(id)) repository.findById(id).get()
        else throw EventNotFoundException("Not found event with id $id")
    }

    fun getAttendedEvents(username: String): EventsDTO {
        if (userService.existsByUsername(username)) {
            val user = User.fromDto(userService.getByUsername(username))

            return EventsDTO(repository.findByAttendees(user).map { it.toDto() }, 0, 0)
        } else throw Exception("Exception")
    }

    fun getCreatedEvents(username: String): EventsDTO {
        if (userService.existsByUsername(username)) {
            val user = User.fromDto(userService.getByUsername(username))

            return EventsDTO(repository.findByUser(user).map { it.toDto() }, 0, 0)
        } else throw Exception("Exception")
    }

    fun getByDistance(latitud: Double, longitud: Double, distance: Double, page: Int, size: Int?): EventsDTO {
        var response = repository.findByDistance(latitud, longitud, distance, page, if (size == null) 1000 else size!!)
        return EventsDTO(response.map { it.toDto() }, page, response.size)
    }

    fun create(username: String, event: EventDTO): Event {
        val user = User.fromDto(userService.getByUsername(username))
        event.createdDate = LocalDateTime.now()
        event.lastUpdate = event.createdDate
        event.attendeesCount = 0
        event.likes = 0
        event.dislikes = 0
        return repository.save(Event.fromDto(event, user))
    }

    fun remove(username: String, id: Long) {
        if (attendanceService.deleteAttendancesOnDeleteEvent(username, id, true)
                || likeService.deleteLikesOnDeleteEvent(username, id)
                || likeService.deleteDislikesOnDeleteEvent(username, id))
            repository.deleteById(id)
        else
            throw EventNotFoundException("Not found event with id $id")
        var found = false
        for (i in 0..userService.getByUsername(username).roles.size - 1) {
            println(userService.getByUsername(username).roles.toList()[i].name)
            found = userService.getByUsername(username).roles.toList()[i].name.equals("admin")
        }
        if ((repository.existsById(id) && repository.findById(id).get().user.id == userService.getByUsername(username).id) || found) {
            if (attendanceService.deleteAttendancesOnDeleteEvent(username, id, found))
                repository.deleteById(id)
            else
                throw EventNotFoundException("Not found event with id $id")
        }
    }

    fun update(username: String, id: Long, event: EventDTO): Event {
        return if (repository.existsById(id) && repository.findById(id).get().user.id == userService.getByUsername(username).id) {
            event.lastUpdate = LocalDateTime.now()
            repository.save(Event.fromDto(event, repository.findById(id).get()))
        } else throw EventNotFoundException("Not found event with id $id")
    }

    fun report(id: Long, reported: Boolean): Event {
        if (reported) {
            var reportedEvent: Event? = repository.findByIdOrNull(id)
            return if (reportedEvent != null) {
                reportedEvent.lastUpdate = LocalDateTime.now()
                reportedEvent.reported = reported
                repository.save(reportedEvent)
            } else throw EventNotFoundException("Not found event with id $id")
        }
        else if (!reported) {
            var found = false
            val list = userService.getByUsername(SecurityContextHolder.getContext().authentication.name).roles.toList()
            for (i in 0..list.size - 1) {
                found = list[i].name.equals("admin")
            }
            if (found) {
                var reportedEvent: Event? = repository.findByIdOrNull(id)
                return if (reportedEvent != null) {
                    reportedEvent.lastUpdate = LocalDateTime.now()
                    reportedEvent.reported = reported
                    repository.save(reportedEvent)
                } else throw EventNotFoundException("Not found event with id $id")
            }
            else throw Exception("Not an admin")
        }
        else throw EventNotFoundException("Not found event with id $id")
    }

    fun getReported(page: Int, size: Int?, title: String?): EventsDTO {
        var found = false
        val list = userService.getByUsername(SecurityContextHolder.getContext().authentication.name).roles.toList()
        for (i in 0..list.size - 1) {
            found = list[i].name.equals("admin")
        }
        if (found) {
            var events: Page<Event>
            if (title != null)
                events = repository.findByTitleContainingAndReportedIsTrue(
                    title,
                    PageRequest.of(page, size ?: repository.count().toInt())
                )
            else
                events = repository.findByReportedIsTrue(PageRequest.of(page, size ?: repository.count().toInt()))

            val eventsContent = events.content.map {
                it.toDto()
            }

            return EventsDTO(eventsContent, events.number, events.size)
        }
        else throw Exception("Not an admin")
    }
}