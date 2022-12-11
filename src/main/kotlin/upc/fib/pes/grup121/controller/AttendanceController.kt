package upc.fib.pes.grup121.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import upc.fib.pes.grup121.dto.Attendance.AttendanceDTO
import upc.fib.pes.grup121.service.AttendanceService

@RestController
@RequestMapping("attendance")
class AttendanceController(val service: AttendanceService) {
    @GetMapping
    fun getAttendance(@RequestParam eventId: Long): ResponseEntity<Boolean> {
        val username = SecurityContextHolder.getContext().authentication.name
        return ResponseEntity.ok().body(service.getExists(username, eventId))
    }

    @PostMapping
    fun saveAttendance(@RequestBody attendance: AttendanceDTO): ResponseEntity<AttendanceDTO> {
        val username = SecurityContextHolder.getContext().authentication.name
        return ResponseEntity.ok().body(service.create(username, attendance))
    }

    @DeleteMapping
    fun deleteAttendance(@RequestParam eventId: Long): ResponseEntity<AttendanceDTO> {
        val username = SecurityContextHolder.getContext().authentication.name
        return ResponseEntity.ok().body(service.delete(username, eventId))
    }
}