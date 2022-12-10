package upc.fib.pes.grup121.dto.Events

import upc.fib.pes.grup121.model.Event
import java.time.LocalDateTime

data class AgendaEventDTO(
    var codi: Long?,
    var data_fi: LocalDateTime,
    var data_inici: LocalDateTime?,
    var data_fi_aproximada: String?,
    var denominaci: String?,
    var descripcio: String?,
    var entrades: String?,
    var horari: String?,
    var subt_tol : String?,
    var tags_mbits : String?,
    var tags_categor_es : String?,
    var tags_altres_categor_es : String?,
    var enlla_os : String?,
    var documents : String?,
    var imatges : String?,
    var v_deos : String?,
    var adre_a : String?,
    var codi_postal : String?,
    var comarca_i_municipi : String?,
    var email : String?,
    var espai : String?,
    var latitud : String?,
    var localitat : String?,
    var longitud : String?,
    var regi_o_pa_s : String?,
    var tel_fon : String?,
    var url : String?,
    var imgapp : String?,
    var descripcio_html : String?
){
    fun toEvent(): Event = Event(
        id = null,
        title = this.denominaci!!,
        description = this.descripcio,
        subtitle = this.subt_tol,
        initDate = this.data_inici!!,
        endDate = this.data_fi,
        link = this.url,
        placeName = this.comarca_i_municipi,
        location = this.latitud+','+this.longitud,
        address = this.adre_a,
        agendaEventCode = this.codi,
        lastUpdate = null,
        createdDate = null,
        attendees = mutableListOf(),
    )
}