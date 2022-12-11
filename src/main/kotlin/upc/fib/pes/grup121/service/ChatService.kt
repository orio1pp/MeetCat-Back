package upc.fib.pes.grup121.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import upc.fib.pes.grup121.dto.Chats.ChatDTO
import upc.fib.pes.grup121.dto.Chats.GetChatsDTO

@Service
class ChatService(
    val userService: UserService
) {
    private final var restTemplate: RestTemplate = RestTemplate()
    @Value("\${chats.url}")
    lateinit var chatsUrl: String

    fun getChatByusername(username: String):MutableList<GetChatsDTO>?{
        val response:ResponseEntity<MutableList<GetChatsDTO>> =  restTemplate.exchange(chatsUrl+"chat/" +
                username,
            HttpMethod.GET, null, object : ParameterizedTypeReference<MutableList<GetChatsDTO>>(){}
        )
        return response.body
    }
    fun getChatByFriendship(friendshipId: Long): ChatDTO? {
        return restTemplate.getForObject<ChatDTO>(chatsUrl+"chat?friendshipId="+friendshipId);
    }

    fun getAllChats(username: String): List<GetChatsDTO>?{
        val response: ResponseEntity<List<GetChatsDTO>> = restTemplate.exchange(
            chatsUrl+"chats?username="+username,
            HttpMethod.GET, null, object : ParameterizedTypeReference<List<GetChatsDTO>>() {})
        return response.body
    }

    fun insertChat(chat: ChatDTO){
        val request = HttpEntity(chat)
        restTemplate.postForLocation(chatsUrl+"chat", request);
    }

    fun deleteChat(chatId: Long, userName: String){
        userService.getByUsername(userName).let {
            restTemplate.delete(chatsUrl+"chat?chatId="+chatId+"&userName="+ userName);
        }
    }

}
