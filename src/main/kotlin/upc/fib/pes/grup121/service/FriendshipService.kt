package upc.fib.pes.grup121.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import upc.fib.pes.grup121.dto.FriendshipDTO
import java.lang.Exception


@Service
class FriendshipService(
    private final val userService: UserService
) {
    private final val restTemplate: RestTemplate = RestTemplate()
    var urlChats: String = "http://localhost:7070/friendship";
    fun getFriendshipsbyUsername(userNameOnwer: String): List<FriendshipDTO>? {
        var result: List<FriendshipDTO>? =
            restTemplate.getForObject<List<FriendshipDTO>>(urlChats + "?username=" + userNameOnwer);
        return result;
    }

    fun insertFriendship(username: String): FriendshipDTO? {
        try {
            userService.getByUsername(username).let {
                val ownerId: String = SecurityContextHolder.getContext().authentication.name;
                val friendShip: FriendshipDTO = FriendshipDTO(username, ownerId)
                val request = HttpEntity(friendShip)
                restTemplate?.postForLocation(urlChats, request)
                return friendShip;
            }
            return null;
        } catch (e: Exception) {
            return null;
        }
    }
}
