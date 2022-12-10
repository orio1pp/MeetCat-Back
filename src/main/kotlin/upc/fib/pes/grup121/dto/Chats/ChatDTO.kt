package upc.fib.pes.grup121.dto.Chats

data class ChatDTO(
    var chatId:Long,
    var friendship:Long,
    var messageList: List<Long>?
) {
}