package upc.fib.pes.grup121.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import upc.fib.pes.grup121.dto.AttendanceDTO
import upc.fib.pes.grup121.dto.ChatDTO
import upc.fib.pes.grup121.service.AttendanceService

@RestController
@RequestMapping("attendances")
class AttendanceController(val service: AttendanceService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun saveAttendance(@RequestBody attendance: AttendanceDTO){
        service.create(attendance)
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAttendance(@RequestBody attendance: AttendanceDTO){
        service.create(attendance)
    }
}