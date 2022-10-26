package upc.fib.pes.grup121.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import upc.fib.pes.grup121.model.Esdeveniment
import upc.fib.pes.grup121.model.EsdevenimentDTO
import upc.fib.pes.grup121.repository.EsdevenimentRepository
import java.time.LocalDateTime

@Service
class EsdevenimentService(val repository: EsdevenimentRepository) {

    fun getAll(): List<Esdeveniment> = repository.findAll()

    fun getById(id: Long): Esdeveniment = repository.findById(id).get()

    fun create(esdeveniment: EsdevenimentDTO): Esdeveniment {
        esdeveniment.createdDate = LocalDateTime.now()
        esdeveniment.lastUpdate = esdeveniment.createdDate
        return repository.save(Esdeveniment.fromDto(esdeveniment))
    }

    fun remove(id: Long) {
        if (repository.existsById(id)) repository.deleteById(id)
        else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    fun update(id: Long, esdeveniment: EsdevenimentDTO): Esdeveniment {
        return if (repository.existsById(id)) {
            esdeveniment.lastUpdate = LocalDateTime.now()
            repository.save(Esdeveniment.fromDto(esdeveniment, repository.findById(id).get()))
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }
}