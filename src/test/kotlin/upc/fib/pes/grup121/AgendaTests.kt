package upc.fib.pes.grup121

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import upc.fib.pes.grup121.agenda.AgendaEventService
import java.time.LocalDateTime

@SpringBootTest(
    classes = arrayOf(Grup121Application::class),
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Disabled
class AgendaTests (@Autowired val agendaService: AgendaEventService) {


    @Test
    fun test() {
    //    var agenda = agendaService.getAllAgendaEvents()
   //     assertNotNull(agenda)
      //  agendaService.updateData()
    }

}