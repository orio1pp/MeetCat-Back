package upc.fib.pes.grup121.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import upc.fib.pes.grup121.dto.CommentDTO
import upc.fib.pes.grup121.dto.EventsDTO
import upc.fib.pes.grup121.dto.FriendshipDTO
import upc.fib.pes.grup121.dto.UserDTO
import upc.fib.pes.grup121.exception.EventNotFoundException
import upc.fib.pes.grup121.model.Comment
import upc.fib.pes.grup121.model.Event
import upc.fib.pes.grup121.model.EventDTO
import upc.fib.pes.grup121.repository.EventRepository
import java.time.LocalDateTime

@Service
class EventService(
    val repository: EventRepository, private final val userService: UserService
) {


    fun getPaginated(page: Int, size: Int?): EventsDTO {
        val events = repository.findAll(PageRequest.of(page, size ?: repository.count().toInt()))
        return EventsDTO(events.content, events.number, events.size)
    }

    fun getById(id: Long): Event {
        return if (repository.existsById(id)) repository.findById(id).get()
        else throw EventNotFoundException("Not found event with id $id")
    }

    fun create(event: EventDTO): Event {
        event.createdDate = LocalDateTime.now()
        event.lastUpdate = event.createdDate
        return repository.save(Event.fromDto(event))
    }

    fun remove(id: Long) {
        if (repository.existsById(id)) repository.deleteById(id)
        else throw EventNotFoundException("Not found event with id $id")
    }

    fun update(id: Long, event: EventDTO): Event {
        return if (repository.existsById(id)) {
            event.lastUpdate = LocalDateTime.now()
            repository.save(Event.fromDto(event, repository.findById(id).get()))
        } else throw EventNotFoundException("Not found event with id $id")
    }

    fun likeEvent(id: Long, username: String): Event? {
        return if (repository.existsById(id)) {
            var event = repository.findById(id).get()
            try {
                userService.getByUsername(username).let {
                    if (event.likedByUserList.contains(it)) {
                        event.likedByUserList.remove(it)
                    } else {
                        event.likedByUserList.add(it)
                        if (event.dislikedByUserList.contains(it)) {
                            event.dislikedByUserList.remove(it)
                        }
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
                    if (event.dislikedByUserList.contains(it)) {
                        event.dislikedByUserList.remove(it)
                    } else {
                        event.dislikedByUserList.add(it)
                        if (event.likedByUserList.contains(it)) {
                            event.likedByUserList.remove(it)
                        }
                    }
                    repository.save(event)
                    return event
                }
            } catch (e: Exception) {
                return null;
            }
        } else throw EventNotFoundException("Not found event with id $id")
    }

    fun addComment(id: Long, commentDTO: CommentDTO): Event? {
        return if (repository.existsById(id)) {
            var event = repository.findById(id).get()
            try {
                event.commments.add(Comment.fromDTO(commentDTO))
                repository.save(event)
                return event
            } catch (e: Exception) {
                return null;
            }
        } else throw EventNotFoundException("Not found event with id $id")
    }
}