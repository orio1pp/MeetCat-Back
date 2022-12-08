package upc.fib.pes.grup121.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import upc.fib.pes.grup121.dto.MessageDTO
import upc.fib.pes.grup121.dto.MessagesDTO
import upc.fib.pes.grup121.service.MessageService

@RestController
class MessageController(
    private final var messageService: MessageService
) {
    @GetMapping("message")
    fun getMessagesById(@RequestBody messagesDTO: MessagesDTO): ResponseEntity<List<MessageDTO>> {
        val username: String = SecurityContextHolder.getContext().authentication.name
        messagesDTO.username =username
        var messages: List<MessageDTO>? = messageService.getMessagesById(messagesDTO)
        messages.let{
            return ResponseEntity.ok(it);
        }
        return ResponseEntity(null, HttpStatus.BAD_REQUEST)
    }

    @PostMapping("message")
    @SendTo("/topic/message")
    fun insertNewMessage(@RequestBody message: MessageDTO){
        val username: String = SecurityContextHolder.getContext().authentication.name;
        message.let{
            return messageService.insertNewMessage(message, username);
        }
    }
}