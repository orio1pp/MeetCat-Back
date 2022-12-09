package upc.fib.pes.grup121.dto.Messages

class GetMessagesDTO(
    val chatId: Long,
    var username:String,
    val page: Int,
    val size: Int
) {
}