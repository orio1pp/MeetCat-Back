package upc.fib.pes.grup121.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
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
) {
    fun getLiked(username: String, eventId: Long): Boolean {
        if (repository.existsById(eventId)) {
            val event = repository.findById(eventId).get()
            if (userService.existsByUsername(username)) {
                val user = User.fromDto(userService.getByUsername(username))
                if (event.likedByUserList.contains(user))
                    return true
                return false
            }
            else throw UserNotFoundException("User with id ${username} not found")
        }
        else throw EventNotFoundException("Event with id ${eventId} not found")
    }

    fun getDisliked(username: String, eventId: Long): Boolean {
        if (repository.existsById(eventId)) {
            val event = repository.findById(eventId).get()
            if (userService.existsByUsername(username)) {
                val user = User.fromDto(userService.getByUsername(username))
                if (event.dislikedByUserList.contains(user))
                    return true
                return false
            }
            else throw UserNotFoundException("User with id ${username} not found")
        }
        else throw EventNotFoundException("Event with id ${eventId} not found")
    }

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

    fun getByDistance(latitud: Double, longitud: Double, distance: Double, page: Int, size: Int?): EventsDTO{
        var response = repository.findByDistance(latitud, longitud, distance, page, if(size ==null) 1000 else size!!)
        return EventsDTO(response.map { it.toDto() }, page, response.size)
    }

    fun create(username: String, event: EventDTO): Event {
        val user = User.fromDto(userService.getByUsername(username))
        event.createdDate = LocalDateTime.now()
        event.lastUpdate = event.createdDate
        event.attendeesCount = 0
        return repository.save(Event.fromDto(event, user))
    }

    fun remove(username: String, id: Long) {
        var found = false
        for (i in 0..userService.getByUsername(username).roles.size - 1) {
            println(userService.getByUsername(username).roles.toList()[i].name)
            found = userService.getByUsername(username).roles.toList()[i].name.equals("ADMIN")
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
        var reportedEvent: Event? = repository.findByIdOrNull(id)
        return if (reportedEvent != null) {
            reportedEvent.lastUpdate = LocalDateTime.now()
            reportedEvent.reported = reported
            repository.save(reportedEvent)
        } else throw EventNotFoundException("Not found event with id $id")
    }

    fun getReported(page: Int, size: Int?, title: String?): EventsDTO {
        var events: Page<Event>
        if (title != null)
            events = repository.findByTitleContainingAndReportedIsTrue(title, PageRequest.of(page, size ?: repository.count().toInt()))
        else
            events = repository.findByReportedIsTrue(PageRequest.of(page, size ?: repository.count().toInt()))

        val eventsContent = events.content.map {
            it.toDto()
        }

        return EventsDTO(eventsContent, events.number, events.size)
    }

    fun likeEvent(id: Long, username: String): Event? {
        return if (repository.existsById(id)) {
            var event = repository.findById(id).get()
            try {
                userService.getByUsername(username).let {
                    if (event.likedByUserList.contains(User.fromDto(it))) {
                        event.likedByUserList.remove(User.fromDto(it))
                    } else {
                        event.likedByUserList.add(User.fromDto(it))
                    }
                    if (event.dislikedByUserList.contains(User.fromDto(it))) {
                        event.dislikedByUserList.remove(User.fromDto(it))
                    }
                    repository.save(event)
                    return event
                }
            } catch (e: Exception) {
                return null;
            }
        } else throw EventNotFoundException("Not found event with id $id")
    }

    fun dislikeEvent(id: Long, username: String): Event? {
        return if (repository.existsById(id)) {
            var event = repository.findById(id).get()
            try {
                userService.getByUsername(username).let {
                    if (event.dislikedByUserList.contains(User.fromDto(it))) {
                        event.dislikedByUserList.remove(User.fromDto(it))
                    } else {
                        event.dislikedByUserList.add(User.fromDto(it))
                    }
                    if (event.likedByUserList.contains(User.fromDto(it))) {
                        event.likedByUserList.remove(User.fromDto(it))
                    }
                    repository.save(event)
                    return event
                }
            } catch (e: Exception) {
                return null;
            }
        } else throw EventNotFoundException("Not found event with id $id")
    }
}