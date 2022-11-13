package upc.fib.pes.grup121.agenda

import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.*


@Service
class AgendaEventService() {

    fun updateData() {

    }

    fun getAllAgendaEvents(){
        var uri = "https://analisi.transparenciacatalunya.cat/resource/rhpv-yr4f.json"
        var restTemplate = RestTemplate()
        var headers = org.springframework.http.HttpHeaders()
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON))
        val entity = HttpEntity("body", headers)
        val typeRef: ParameterizedTypeReference<List<AgendaEventDTO?>?> =
            object : ParameterizedTypeReference<List<AgendaEventDTO?>?>() {}
        val result = restTemplate.exchange(uri, HttpMethod.GET, entity, typeRef)
        val eventList: List<AgendaEventDTO> = result.body as List<AgendaEventDTO>
        println("Result events agenda: " + eventList.size)
    }

}