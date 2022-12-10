package upc.fib.pes.grup121.model

import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime
import javax.persistence.*
import upc.fib.pes.grup121.dto.CommentDTO

@Entity
@DynamicUpdate
@Table(name = "comment")
data class Comment (
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long?,
    var username: String,
    var commentText: String,
    var createdDate: LocalDateTime? = null,
    var lastUpdate: LocalDateTime? = null
){
    companion object {
        fun fromDTO(dto: CommentDTO) = Comment(
            username = dto.username,
            commentText = dto.commentText,
            createdDate = dto.createdDate,
            lastUpdate = dto.lastUpdate,
            id = null
        )
    }

}