package upc.fib.pes.grup121.dto.Chats

data class GetChatsDTO(
    val chatId: Long?,
    val friend: String?,
    val friendship: Long?
) {
}