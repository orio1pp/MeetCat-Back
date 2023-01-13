package upc.fib.pes.grup121.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import upc.fib.pes.grup121.dto.Attendance.AttendanceDTO
import upc.fib.pes.grup121.model.Event
import upc.fib.pes.grup121.service.AttendanceService
import upc.fib.pes.grup121.service.LikeService

@RestController
@RequestMapping("like")
class LikeController(val service: LikeService) {
    @GetMapping("/{id}/liked")
    fun getLiked(@PathVariable id: Long): ResponseEntity<Boolean>{
        val username = SecurityContextHolder.getContext().authentication.name
        return ResponseEntity.ok().body(service.getLiked(username, id))
    }

    @GetMapping("/{id}/disliked")
    fun getDisliked(@PathVariable id: Long): ResponseEntity<Boolean>{
        val username = SecurityContextHolder.getContext().authentication.name
        return ResponseEntity.ok().body(service.getDisliked(username, id))
    }

    @PutMapping("/{id}/like")
    fun likeEvent(@PathVariable id: Long): ResponseEntity<Long> {
        val username: String = SecurityContextHolder.getContext().authentication.name
        return ResponseEntity.ok().body(service.like(username, id))
    }

    @PutMapping("/{id}/dislike")
    fun dislikeEvent(@PathVariable id: Long): ResponseEntity<Long> {
        val username: String = SecurityContextHolder.getContext().authentication.name
        return ResponseEntity.ok().body(service.dislike(username, id))
    }

    @DeleteMapping("/{id}/like")
    fun deleteLike(@PathVariable id: Long): ResponseEntity<Long> {
        val username = SecurityContextHolder.getContext().authentication.name
        return ResponseEntity.ok().body(service.deleteLike(username, id))
    }

    @DeleteMapping("/{id}/dislike")
    fun deleteDislike(@PathVariable id: Long): ResponseEntity<Long> {
        val username = SecurityContextHolder.getContext().authentication.name
        return ResponseEntity.ok().body(service.deleteDislike(username, id))
    }

}