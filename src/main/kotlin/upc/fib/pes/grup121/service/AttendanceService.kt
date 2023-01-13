package upc.fib.pes.grup121.service

import org.springframework.stereotype.Service
import upc.fib.pes.grup121.dto.Attendance.AttendanceDTO
import upc.fib.pes.grup121.exception.EventNotFoundException
import upc.fib.pes.grup121.exception.UserNotFoundException
import upc.fib.pes.grup121.model.User
import upc.fib.pes.grup121.repository.EventRepository
import upc.fib.pes.grup121.repository.UserRepository

@Service
class AttendanceService(
        val userRepository: UserRepository,
        val eventRepository: EventRepository,
) {
    fun getExists(username: String, eventId: Long): Boolean {
        if (eventRepository.existsById(eventId)) {
            val event = eventRepository.findById(eventId).get()
            if (userRepository.existsByUsername(username)) {
                val user = userRepository.findByUsername(username)
                if (user.attendingEvents.contains(event))
                    return true
                return false
            }
            else throw UserNotFoundException("User with id ${username} not found")
        }
        else throw EventNotFoundException("Event with id ${eventId} not found")
    }

    fun create(username: String, attendance: AttendanceDTO): AttendanceDTO {
        return AttendanceDTO(eventId = updateAttendingEvents(username, attendance.eventId, true))
    }

    fun delete(username: String, eventId: Long): AttendanceDTO {
        return AttendanceDTO(eventId = updateAttendingEvents(username, eventId, false))
    }

    fun deleteAttendancesOnDeleteEvent(username: String, eventId: Long, found: Boolean): Boolean {
        if (found || (eventRepository.existsById(eventId)
            && eventRepository.findById(eventId).get().user.id == userRepository.findByUsername(username).id)) {
            val event = eventRepository.findById(eventId).get()
            val users = userRepository.findByAttendingEvents(event)
            users.forEach { user ->
                delete(user.username, eventId)
            }
            return true
        }
        return false
    }

    private fun updateAttendingEvents(username: String, eventId: Long, isCreate: Boolean): Long {
        if (eventRepository.existsById(eventId)) {
            val event = eventRepository.findById(eventId).get()
            if (userRepository.existsByUsername(username)) {
                val user = userRepository.findByUsername(username)
                if (isCreate && !user.attendingEvents.contains(event)) {
                    user.attendingEvents.add(event)
                    userRepository.save(user)

                    ++event.attendeesCount
                    eventRepository.save(event)
                    return eventId
                }
                else if (!isCreate && user.attendingEvents.contains(event)) {
                    user.attendingEvents.remove(event)
                    userRepository.save(user)

                    --event.attendeesCount
                    eventRepository.save(event)
                    return eventId
                }
                else return eventId
            }
            else throw UserNotFoundException("User with id ${username} not found")
        } else throw EventNotFoundException("Event with id ${eventId} not found")
    }
}