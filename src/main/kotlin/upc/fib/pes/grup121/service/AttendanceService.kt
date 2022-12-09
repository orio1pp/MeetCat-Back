package upc.fib.pes.grup121.service

import org.springframework.stereotype.Service
import upc.fib.pes.grup121.dto.AttendanceDTO
import upc.fib.pes.grup121.exception.UserNotFoundException
import upc.fib.pes.grup121.repository.UserRepository

@Service
class AttendanceService(
        val userRepository: UserRepository,
        val eventService: EventService,
) {
    fun create(attendanceDTO: AttendanceDTO) {
        try {
            val event = eventService.getById(attendanceDTO.eventId)
            if (userRepository.existsById(attendanceDTO.userId)) {
                val user = userRepository.findById(attendanceDTO.userId).get()
                if (!user.attendingEvents.contains(event)) {
                    user.attendingEvents.add(event)
                    userRepository.save(user)
                }
            }
            else throw UserNotFoundException("User with id $attendanceDTO.userId not found")
        } catch (e: Exception) {
            throw e
        }


    }
}