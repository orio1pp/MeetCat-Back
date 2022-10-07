package upc.fib.pes.grup121.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import upc.fib.pes.grup121.model.Esdeveniment
import upc.fib.pes.grup121.repository.EsdevenimentRepository

@Service
class EsdevenimentService(val repository: EsdevenimentRepository) {

    fun getAll(): List<Esdeveniment> = repository.findAll()

    fun getById(id: Long): Esdeveniment = repository.findById(id).get()

    fun create(esdeveniment: Esdeveniment): Esdeveniment = repository.save(esdeveniment)

    fun remove(id: Long) {
        if (repository.existsById(id)) repository.deleteById(id)
        else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    fun update(id: Long, esdeveniment: Esdeveniment): Esdeveniment {
        return if (repository.existsById(id)) {
            esdeveniment.id = id
            repository.save(esdeveniment)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }
}