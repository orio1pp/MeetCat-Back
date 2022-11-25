package upc.fib.pes.grup121.model

import org.hibernate.annotations.DynamicUpdate
import upc.fib.pes.grup121.dto.RoleDTO
import javax.persistence.*

@Entity
@DynamicUpdate
@Table(name = "roles")
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) var id: Long?,
    var name: String? = null
) {
    fun toDto(): RoleDTO = RoleDTO(
        id = this.id,
        name = this.name
    )

    companion object {
        fun fromDto(dto: RoleDTO) : Role {
            return Role(
                id = dto.id,
                name = dto.name
            )
        }

        fun fromDto(dto: RoleDTO, default: Role) : Role {
            return Role(
                id = default.id,
                name = dto.name ?: default.name
            )
        }
    }
}