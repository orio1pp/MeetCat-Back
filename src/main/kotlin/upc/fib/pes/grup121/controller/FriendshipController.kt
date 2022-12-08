package upc.fib.pes.grup121.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import upc.fib.pes.grup121.dto.FriendshipDTO
import upc.fib.pes.grup121.dto.FriendshipsDTO
import upc.fib.pes.grup121.service.FriendshipService

@RestController
class FriendshipController(
    private final var friendshipService: FriendshipService
) {
    @GetMapping("friendship")
    fun getFriendshipsbyUsername(@RequestBody friendshipsDTO: FriendshipsDTO): ResponseEntity<List<FriendshipDTO>>? {
        var friends: List<FriendshipDTO>? =friendshipService.getFriendshipsbyUsername(friendshipsDTO);
        friends.let {
            return ResponseEntity.ok(it)
        }
        return ResponseEntity(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("friendship")
    fun insertFriendship(@RequestBody friendship: FriendshipDTO){
        friendshipService.insertFriendship(friendship);
    }

    @DeleteMapping("friendship")
    fun deleteFriend(@RequestParam friendId: String){
        val name: String = SecurityContextHolder.getContext().authentication.name
        friendshipService.deleteFriendShip(friendId, name);
    }
}