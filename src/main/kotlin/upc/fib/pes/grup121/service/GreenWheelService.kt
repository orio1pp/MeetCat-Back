package upc.fib.pes.grup121.service

import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import upc.fib.pes.grup121.dto.GreenWheel.BikeDTO
import upc.fib.pes.grup121.dto.GreenWheel.ChargerDTO
import upc.fib.pes.grup121.repository.EventRepository
import java.util.*

@Service
class GreenWheelService(val repository: EventRepository, val eventService: EventService, val userService: UserService) {

    fun getChargers(
        current: Int?,
        speed: Int?,
        type: Int?,
        price: Int?,
        order: String?,
        latitude: Double?,
        longitude: Double?,
        zoom: Double?
    ): List<ChargerDTO> {
        var uri = "http://3.253.100.204/api/chargers?latitude=$latitude&longitude=$longitude&distance=$zoom"
        var restTemplate = RestTemplate()
        var headers = org.springframework.http.HttpHeaders()
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON))
        headers.add("API-KEY", "r}PDN1C(_UJ9!&o,5PT`-y9#}Aaj9QoU")
        val entity = HttpEntity("body", headers)
        val typeRef: ParameterizedTypeReference<List<ChargerDTO?>?> =
            object : ParameterizedTypeReference<List<ChargerDTO?>?>() {}
        val result = restTemplate.exchange(uri, HttpMethod.GET, entity, typeRef)
        return result.body as List<ChargerDTO>
    }

    fun getBikes(
        type: Int?,
        price: Int?,
        order: String?,
        latitude: Double?,
        longitude: Double?,
        zoom: Double?
    ): List<BikeDTO> {
        var uri = "http://3.253.100.204/api/bikes?latitude=$latitude&longitude=$longitude&distance=$zoom"
        var restTemplate = RestTemplate()
        var headers = org.springframework.http.HttpHeaders()
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON))
        headers.add("API-KEY", "r}PDN1C(_UJ9!&o,5PT`-y9#}Aaj9QoU")
        val entity = HttpEntity("body", headers)
        val typeRef: ParameterizedTypeReference<List<BikeDTO?>?> =
            object : ParameterizedTypeReference<List<BikeDTO?>?>() {}
        val result = restTemplate.exchange(uri, HttpMethod.GET, entity, typeRef)
        return result.body as List<BikeDTO>
    }
}

