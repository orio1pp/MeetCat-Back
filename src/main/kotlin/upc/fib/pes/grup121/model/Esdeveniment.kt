package upc.fib.pes.grup121.model

import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@DynamicUpdate
@Table(name = "esdeveniments")
data class Esdeveniment(
    @Id  @GeneratedValue(strategy = GenerationType.AUTO) var id: Long?,
    var titol: String,
    var subtitol: String?,
    var descripcio: String?,
    var dataInici: LocalDateTime,
    var dataFi: LocalDateTime?,
    var link: String?,
    var nomLloc: String?,
    var coordenades: String?,
    var address: String?,
    var lastUpdate: LocalDateTime? = null,
    var createdDate: LocalDateTime? = null
){
    fun toDto(): EsdevenimentDTO = EsdevenimentDTO(
        id = this.id!!,
        titol = this.titol,
        descripcio = this.descripcio,
        lastUpdate = this.lastUpdate,
        createdDate = this.createdDate,
        subtitol = this.subtitol,
        dataInici = this.dataInici,
        dataFi = this.dataFi,
        link = this.link,
        nomLloc = this.nomLloc,
        coordenades = this.coordenades,
        address = this.address
    )


    companion object {

        fun fromDto(dto: EsdevenimentDTO) = Esdeveniment(
            id = dto.id,
            titol = dto.titol,
            descripcio = dto.descripcio,
            lastUpdate = dto.lastUpdate,
            createdDate = dto.createdDate,
            subtitol = dto.subtitol,
            dataInici = dto.dataInici,
            dataFi = dto.dataFi,
            link = dto.link,
            nomLloc = dto.nomLloc,
            coordenades = dto.coordenades,
            address = dto.address
  )

        fun fromDto(dto: EsdevenimentDTO, default: Esdeveniment) = Esdeveniment(
            id = default.id!!,
            titol = dto.titol  ?: default.titol,
            descripcio = dto.descripcio ?: default.descripcio,
            lastUpdate = dto.lastUpdate  ?: default.lastUpdate,
            createdDate = dto.createdDate  ?: default.createdDate,
            subtitol = dto.subtitol ?: default.subtitol,
            dataInici = dto.dataInici ?: default.dataInici,
            dataFi = dto.dataFi ?: default.dataFi,
            link = dto.link ?: default.link,
            nomLloc = dto.nomLloc ?: default.nomLloc,
            coordenades = dto.coordenades ?: default.coordenades,
            address = dto.address ?: default.address
        )

    }

}