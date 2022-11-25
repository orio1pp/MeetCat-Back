package upc.fib.pes.grup121.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import upc.fib.pes.grup121.dto.MessageDTO
import upc.fib.pes.grup121.service.MessageService

@RestController
class MessageController(
    private final var messageService: MessageService
) {
    @GetMapping("message")
    fun getMessagesById(@RequestParam chatId: Long): ResponseEntity<List<MessageDTO>> {
        var messages: List<MessageDTO>? = messageService.getMessagesById(chatId)
        messages.let{
            return ResponseEntity.ok(it);
        }
        return ResponseEntity(null, HttpStatus.BAD_REQUEST)
    }

    @PostMapping("message")
    fun insertNewMessage(@RequestBody message: MessageDTO){
        message.let{
            return messageService.insertNewMessage(message);
        }
    }
}