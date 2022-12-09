package upc.fib.pes.grup121.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import upc.fib.pes.grup121.dto.Friendship.FriendshipDTO
import upc.fib.pes.grup121.dto.Friendship.GetFriendshipsDTO


@Service
class FriendshipService(
    private final val userService: UserService
) {
    private val restTemplate: RestTemplate = RestTemplate()
    @Value("\${chats.url}")
    var chatsUrl: String? = null;
    fun getFriendshipsbyUsername(getFriendshipsDTO: GetFriendshipsDTO): List<FriendshipDTO>? {
        val response: ResponseEntity<List<FriendshipDTO>> = restTemplate.exchange(chatsUrl+
                "friendship?username="+getFriendshipsDTO.username +
                "&page=" + getFriendshipsDTO.page  +
                "&size="+ getFriendshipsDTO.size,
            HttpMethod.GET, null, object : ParameterizedTypeReference<List<FriendshipDTO>>() {})

        return response.body;
    }
    fun insertFriendship(friendship: FriendshipDTO) {
        val request = HttpEntity(friendship)
        restTemplate?.postForLocation(chatsUrl+"friendship", request);
    }
    fun deleteFriendShip(friendId:String, ownerId: String){
        userService.getByUsername(friendId).let {
            restTemplate?.delete(chatsUrl + "friendship?friendId=" + friendId + "&ownerId=" + ownerId)
        }
    }
}
