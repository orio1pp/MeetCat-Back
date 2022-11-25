package upc.fib.pes.grup121.service

import org.springframework.http.HttpEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import upc.fib.pes.grup121.dto.FriendshipDTO
import upc.fib.pes.grup121.dto.MessageDTO

@Service
class MessageService(
    private final val restTemplate: RestTemplate = RestTemplate()
) {
    var urlMessages:String = "http://localhost:8081/message"
    fun getMessagesById(chatId: Long): List<MessageDTO>?{
        var result:List<MessageDTO>? = restTemplate.getForObject<List<MessageDTO>>(urlMessages+"?chatId="+chatId);
        return result;
    }

    fun insertNewMessage(message: MessageDTO) {
        val request = HttpEntity(message)
        restTemplate?.postForLocation(urlMessages, request);
    }
}