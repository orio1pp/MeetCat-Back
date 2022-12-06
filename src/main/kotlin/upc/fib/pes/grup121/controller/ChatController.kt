package upc.fib.pes.grup121.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import upc.fib.pes.grup121.dto.ChatDTO
import upc.fib.pes.grup121.service.ChatService

@RestController
@RequestMapping("chats")
class ChatController(
    private final var chatService: ChatService
) {
    @GetMapping("chat")
    fun getChatByFriendship(@RequestParam friendshipId: Long): ResponseEntity<ChatDTO> {
        var chat: ChatDTO? = chatService.getChatByFriendship(friendshipId)
        chat?.let{
            return ResponseEntity.ok(chat)
        }
        return ResponseEntity(null, HttpStatus.BAD_REQUEST)
    }

    @GetMapping("chats")
    fun getAllChats(): ResponseEntity<List<String>> {
        var userId:String = SecurityContextHolder.getContext().authentication.name;
        var chats: List<String>? = chatService.getAllChats(userId);
        chats.let{
            return ResponseEntity.ok(chats)
        }
        return ResponseEntity(null, HttpStatus.BAD_REQUEST)
    }

    @PostMapping("chat")
    fun insertChat(@RequestBody chat: ChatDTO){
        chat.let{
            chatService.insertChat(it);
        }
    }
}