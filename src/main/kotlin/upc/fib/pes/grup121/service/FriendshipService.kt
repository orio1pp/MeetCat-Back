package upc.fib.pes.grup121.service

import org.springframework.http.HttpEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import upc.fib.pes.grup121.dto.FriendshipDTO


@Service
class FriendshipService(
    private final val restTemplate: RestTemplate = RestTemplate()
) {
    var urlChats: String = "http://localhost:8081/friendship";
    fun getFriendshipsbyUsername(userNameOnwer: String): List<FriendshipDTO>?{
        var result:List<FriendshipDTO>? = restTemplate.getForObject<List<FriendshipDTO>>(urlChats + "?username=" + userNameOnwer);
        return result;
    }
    fun insertFriendship(friendship: FriendshipDTO) {
        val request = HttpEntity(friendship)
        restTemplate?.postForLocation(urlChats, request);
    }
}
