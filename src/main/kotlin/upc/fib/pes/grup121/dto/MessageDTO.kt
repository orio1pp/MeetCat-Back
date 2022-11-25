package upc.fib.pes.grup121.dto

data class MessageDTO(
    var messageId: Long?,
    var text: String?,
    var date:String?,
    var chatId:Long?,
    var username:String
) {
}