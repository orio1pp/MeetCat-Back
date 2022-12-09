package upc.fib.pes.grup121.controller

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import upc.fib.pes.grup121.dto.Friendship.FriendshipDTO
import upc.fib.pes.grup121.dto.Friendship.GetFriendshipsDTO
import upc.fib.pes.grup121.service.FriendshipService


@RestController
class FriendshipController(
    private final var friendshipService: FriendshipService
) {
    @GetMapping("friendship")
    fun getFriendshipsbyUsername(@RequestBody getFriendshipsDTO: GetFriendshipsDTO): List<FriendshipDTO>? {
        val username: String = SecurityContextHolder.getContext().authentication.name
        getFriendshipsDTO.username = username;
        var friends: List<FriendshipDTO>? = friendshipService.getFriendshipsbyUsername(getFriendshipsDTO);
        return friends
    }

    @PostMapping("friendship")
    fun insertFriendship(@RequestBody friendship: FriendshipDTO){
        val username: String = SecurityContextHolder.getContext().authentication.name
        friendship.ownerId = username
        friendshipService.insertFriendship(friendship);
    }

    //@DeleteMapping("friendship")
    @RequestMapping(method = [RequestMethod.DELETE], value = ["friendship"])
    fun deleteFriend(@RequestParam friendId: String){
        val name: String = SecurityContextHolder.getContext().authentication.name
        friendshipService.deleteFriendShip(friendId, name);
    }
}
