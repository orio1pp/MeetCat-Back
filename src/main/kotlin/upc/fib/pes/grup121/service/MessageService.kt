package upc.fib.pes.grup121.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import upc.fib.pes.grup121.dto.Messages.GetMessagesDTO
import upc.fib.pes.grup121.dto.Messages.InsertMessageDTO

@Service
class MessageService(
    private final val restTemplate: RestTemplate = RestTemplate()
) {
    @Value("\${chats.url}")
    lateinit  var chatsUrl: String;
    fun getMessagesById(messagesDTO: GetMessagesDTO): List<InsertMessageDTO>? {

        val response: ResponseEntity<List<InsertMessageDTO>> = restTemplate.exchange(chatsUrl + "message"+
                "?chatId=" + messagesDTO.chatId +
                "&username=" + messagesDTO.username +
                "&size=" + messagesDTO.size +
                "&page=" + messagesDTO.page,
            HttpMethod.GET, null, object : ParameterizedTypeReference<List<InsertMessageDTO>>() {})
        //var result: List<InsertMessageDTO>? = restTemplate.getForObject<List<InsertMessageDTO>>(urlMessages, request);
        return response.body
    }

    fun insertNewMessage(message: InsertMessageDTO, username: String) {
        if (message.username.equals(username)) {
            val request = HttpEntity(message)
            restTemplate?.postForLocation(chatsUrl + "message", request);
        } else
            throw Exception("User it's not the owner of the message");
    }
}