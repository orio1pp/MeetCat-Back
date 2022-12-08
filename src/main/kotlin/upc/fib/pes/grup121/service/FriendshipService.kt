package upc.fib.pes.grup121.service

import org.springframework.http.HttpEntity
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import upc.fib.pes.grup121.dto.FriendshipDTO
import upc.fib.pes.grup121.dto.FriendshipsDTO


@Service
class FriendshipService(
    private final val userService: UserService,
) {
    private val restTemplate: RestTemplate = RestTemplate()
    var urlChats: String = "http://localhost:8081/friendship";
    fun getFriendshipsbyUsername(friendshipsDTO: FriendshipsDTO): List<FriendshipDTO>?{
        val request = HttpEntity(friendshipsDTO)
        var result:List<FriendshipDTO>? = restTemplate.getForObject<List<FriendshipDTO>>(urlChats, request);
        return result;
    }
    fun insertFriendship(friendship: FriendshipDTO) {
        val request = HttpEntity(friendship)
        restTemplate?.postForLocation(urlChats, request);
    }
    fun deleteFriendShip(friendId:String, ownerId: String){
        userService.getByUsername(friendId).let {
            restTemplate?.delete(urlChats + "?friendId=" + friendId + "?ownerId=" + ownerId)
        }
    }
}
