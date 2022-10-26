package upc.fib.pes.grup121.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import upc.fib.pes.grup121.model.Esdeveniment
import upc.fib.pes.grup121.model.EsdevenimentDTO
import upc.fib.pes.grup121.service.EsdevenimentService

@RequestMapping("/esdeveniments")
@RestController
class EsdevenimentController (val service: EsdevenimentService){

    @GetMapping
    fun getEsdeveniments(): List<Esdeveniment> {
        return service.getAll()
    }

    @GetMapping("/{id}")
    fun getEsdeveniment(@PathVariable id: Long) = service.getById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun saveEsdeveniment(@RequestBody esdeveniment: EsdevenimentDTO): Esdeveniment = service.create(esdeveniment)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteEsdeveniment(@PathVariable id: Long) = service.remove(id)

    @PutMapping("/{id}")
    fun updateEsdeveniment(
        @PathVariable id: Long, @RequestBody esdeveniment: EsdevenimentDTO
    ) = service.update(id, esdeveniment)


//        listOf(
//        Esdeveniment("1", "Correbars","descripcio"),
//        Esdeveniment("2", "Concert Rosalia", "descripcio"),
//        Esdeveniment("3", "Calçotada popular","descripcio"),
//    )
}