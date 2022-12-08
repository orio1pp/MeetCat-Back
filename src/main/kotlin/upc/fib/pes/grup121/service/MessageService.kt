package upc.fib.pes.grup121.service

import org.springframework.http.HttpEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import upc.fib.pes.grup121.dto.FriendshipDTO
import upc.fib.pes.grup121.dto.MessageDTO
import upc.fib.pes.grup121.dto.MessagesDTO

@Service
class MessageService(
    private final val restTemplate: RestTemplate = RestTemplate()
) {
    var urlMessages: String = "http://localhost:8081/message"
    fun getMessagesById(messagesDTO: MessagesDTO): List<MessageDTO>? {
        val request = HttpEntity(messagesDTO)
        var result: List<MessageDTO>? = restTemplate.getForObject<List<MessageDTO>>(urlMessages, request);
        return result;
    }
    fun insertNewMessage(message: MessageDTO, username: String) {
        if (message.username.equals(username)) {
            val request = HttpEntity(message)
            restTemplate?.postForLocation(urlMessages, request);
        }
        throw Exception("User it's not the owner of the message");
    }
}