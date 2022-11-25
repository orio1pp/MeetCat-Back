package upc.fib.pes.grup121.dto

data class ChatDTO(
    var chatId:Long,
    var id:Long,
    var messageList: List<Long>
) {
}