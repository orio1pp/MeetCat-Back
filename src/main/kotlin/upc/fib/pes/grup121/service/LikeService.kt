package upc.fib.pes.grup121.service

import org.springframework.stereotype.Service
import upc.fib.pes.grup121.exception.EventNotFoundException
import upc.fib.pes.grup121.exception.UserNotFoundException
import upc.fib.pes.grup121.repository.EventRepository
import upc.fib.pes.grup121.repository.UserRepository

@Service
class LikeService(
        val userRepository: UserRepository,
        val eventRepository: EventRepository,
) {
    fun getLiked(username: String, eventId: Long): Boolean {
        if (eventRepository.existsById(eventId)) {
            val event = eventRepository.findById(eventId).get()
            if (userRepository.existsByUsername(username)) {
                val user = userRepository.findByUsername(username)
                if (event.likedByUserList.contains(user))
                    return true
                return false
            } else throw UserNotFoundException("User with id ${username} not found")
        } else throw EventNotFoundException("Event with id ${eventId} not found")
    }

    fun getDisliked(username: String, eventId: Long): Boolean {
        if (eventRepository.existsById(eventId)) {
            val event = eventRepository.findById(eventId).get()
            if (userRepository.existsByUsername(username)) {
                val user = userRepository.findByUsername(username)
                if (event.dislikedByUserList.contains(user))
                    return true
                return false
            } else throw UserNotFoundException("User with id ${username} not found")
        } else throw EventNotFoundException("Event with id ${eventId} not found")
    }
    fun like(username: String, eventId: Long): Long {
        return updateLikedEvents(username, eventId, true)
    }

    fun deleteLike(username: String, eventId: Long): Long {
        return updateLikedEvents(username, eventId, false)
    }

    fun dislike(username: String, eventId: Long): Long {
        return updateDislikedEvents(username, eventId, true)
    }

    fun deleteDislike(username: String, eventId: Long): Long {
        return updateDislikedEvents(username, eventId, false)
    }

    fun deleteLikesOnDeleteEvent(username: String, eventId: Long): Boolean {
        if (eventRepository.existsById(eventId)
                && eventRepository.findById(eventId).get().user.id == userRepository.findByUsername(username).id) {
            val event = eventRepository.findById(eventId).get()
            val users = userRepository.findByEventsLiked(event)
            users.forEach { user ->
                deleteLike(user.username, eventId)
            }
            return true
        }
        return false
    }

    fun deleteDislikesOnDeleteEvent(username: String, eventId: Long): Boolean {
        if (eventRepository.existsById(eventId)
                && eventRepository.findById(eventId).get().user.id == userRepository.findByUsername(username).id) {
            val event = eventRepository.findById(eventId).get()
            val users = userRepository.findByEventsLiked(event)
            users.forEach { user ->
                deleteDislike(user.username, eventId)
            }
            return true
        }
        return false
    }

    private fun updateLikedEvents(username: String, eventId: Long, isCreate: Boolean): Long {
        if (eventRepository.existsById(eventId)) {
            val event = eventRepository.findById(eventId).get()
            if (userRepository.existsByUsername(username)) {
                val user = userRepository.findByUsername(username)
                if (isCreate && !event.likedByUserList.contains(user)) {
                    event.likedByUserList.add(user)
                    ++event.likes

                    if (event.dislikedByUserList.contains(user)) {
                        event.dislikedByUserList.remove(user)
                        --event.dislikes
                    }

                    eventRepository.save(event)
                    return eventId
                }
                else if (!isCreate && event.likedByUserList.contains(user)) {
                    event.likedByUserList.remove(user)
                    --event.likes
                    eventRepository.save(event)
                    return eventId
                }
                else return eventId
            }
            else throw UserNotFoundException("User with id ${username} not found")
        } else throw EventNotFoundException("Event with id ${eventId} not found")
    }

    private fun updateDislikedEvents(username: String, eventId: Long, isCreate: Boolean): Long {
        if (eventRepository.existsById(eventId)) {
            val event = eventRepository.findById(eventId).get()
            if (userRepository.existsByUsername(username)) {
                val user = userRepository.findByUsername(username)
                if (isCreate && !event.dislikedByUserList.contains(user)) {
                    event.dislikedByUserList.add(user)
                    ++event.dislikes

                    if (event.likedByUserList.contains(user)) {
                        event.likedByUserList.remove(user)
                        --event.likes
                    }

                    eventRepository.save(event)
                    return eventId
                }
                else if (!isCreate && event.dislikedByUserList.contains(user)) {
                    event.dislikedByUserList.remove(user)
                    --event.dislikes
                    eventRepository.save(event)
                    return eventId
                }
                else return eventId
            }
            else throw UserNotFoundException("User with id ${username} not found")
        } else throw EventNotFoundException("Event with id ${eventId} not found")
    }
}