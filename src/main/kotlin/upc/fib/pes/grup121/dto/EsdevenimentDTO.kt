package upc.fib.pes.grup121.model

import java.time.LocalDateTime

data class EsdevenimentDTO(
    var id: Long?,
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

}