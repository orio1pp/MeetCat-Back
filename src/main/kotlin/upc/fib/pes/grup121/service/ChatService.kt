package upc.fib.pes.grup121.service

import org.springframework.http.HttpEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import upc.fib.pes.grup121.dto.ChatDTO

@Service
class ChatService(
    private final var restTemplate: RestTemplate = RestTemplate()
) {
    var urlChats:String = "http://localhost:8081/chat"

    fun getChatByFriendship(friendshipId: Long): ChatDTO? {
        return restTemplate.getForObject<ChatDTO>(urlChats+"?friendshipId="+friendshipId);
    }

    fun getAllChats(userId: Long): List<ChatDTO>?{
        return restTemplate.getForObject<List<ChatDTO>>(urlChats+"?userId="+userId);
    }

    fun insertChat(chat: ChatDTO){
        val request = HttpEntity(chat)
        restTemplate.postForLocation(urlChats, request);
    }

}