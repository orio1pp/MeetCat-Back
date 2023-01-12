package upc.fib.pes.grup121.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import upc.fib.pes.grup121.dto.GreenWheel.BikeDTO
import upc.fib.pes.grup121.dto.GreenWheel.ChargerDTO
import upc.fib.pes.grup121.service.GreenWheelService

@RequestMapping("/greenwheel")
@RestController
class GreenWheelController (val service: GreenWheelService){

    @GetMapping("/chargers")
    fun getChargers(
        @RequestParam("current") current: Int?,
        @RequestParam("speed") speed: Int?,
        @RequestParam("type") type: Int?,
        @RequestParam("price") price: Int?,
        @RequestParam("order") order: String?, //"price","proximity"
        @RequestParam("latitude") latitude: Double?,
        @RequestParam("longitude") longitude: Double?,
        @RequestParam("zoom") zoom: Double?,
    ): List<ChargerDTO> {
        return service.getChargers(current, speed, type, price, order, latitude, longitude, zoom)
    }

    @GetMapping("/bikes")
    fun getBikes(
        @RequestParam("type") type: Int?,
        @RequestParam("price") price: Int?,
        @RequestParam("order") order: String?, //"price","proximity"
        @RequestParam("latitude") latitude: Double?,
        @RequestParam("longitude") longitude: Double?,
        @RequestParam("zoom") zoom: Double?,
    ): List<BikeDTO> {
        return service.getBikes(type, price, order, latitude, longitude, zoom)
    }
}