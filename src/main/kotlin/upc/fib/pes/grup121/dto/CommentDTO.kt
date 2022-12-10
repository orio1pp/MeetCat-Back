package upc.fib.pes.grup121.dto

import java.time.LocalDateTime

data class CommentDTO(
    var username: String,
    var commentText: String,
    var lastUpdate: LocalDateTime? = null,
    var createdDate: LocalDateTime? = null
)