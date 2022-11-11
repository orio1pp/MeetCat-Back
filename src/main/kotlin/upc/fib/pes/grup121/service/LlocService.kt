package upc.fib.pes.grup121.service

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import upc.fib.pes.grup121.model.Lloc
import upc.fib.pes.grup121.repository.LlocRepository
import java.time.LocalDateTime

@Service
class LlocService(val repository: LlocRepository) {

    fun getAll(): List<Lloc> = repository.findAll()

    fun getById(id: Long): Lloc = repository.findById(id).get()

    fun create(lloc: Lloc): Lloc {
        return repository.save(lloc)
    }

    fun remove(id: Long) {
        if (repository.existsById(id)) repository.deleteById(id)
        else throw NotFoundException()
    }

    fun update(id: Long, lloc: Lloc): Lloc {
        return if (repository.existsById(id)) {
            repository.save(lloc)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }
}