package upc.fib.pes.grup121.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import upc.fib.pes.grup121.dto.Messages.InsertMessageDTO
import upc.fib.pes.grup121.dto.Messages.GetMessagesDTO
import upc.fib.pes.grup121.service.MessageService

@RestController
class MessageController(
    private final var messageService: MessageService
) {
    @GetMapping("message")
    fun getMessages(@RequestBody getMessagesDTO: GetMessagesDTO): ResponseEntity<List<InsertMessageDTO>> {
        val username: String = SecurityContextHolder.getContext().authentication.name
        getMessagesDTO.username =username
        var messages: List<InsertMessageDTO>? = messageService.getMessagesById(getMessagesDTO)
        messages.let{
            return ResponseEntity.ok(it);
        }
        return ResponseEntity(null, HttpStatus.BAD_REQUEST)
    }

    @PostMapping("message")
    @SendTo("/topic/message")
    fun insertNewMessage(@RequestBody message: InsertMessageDTO){
        val username: String = SecurityContextHolder.getContext().authentication.name;
        message.let{
            return messageService.insertNewMessage(message, username);
        }
    }
}