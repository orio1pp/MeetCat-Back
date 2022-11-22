package upc.fib.pes.grup121.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import upc.fib.pes.grup121.dto.RoleDTO
import upc.fib.pes.grup121.model.Role
import upc.fib.pes.grup121.service.RoleService
import java.net.URI

@RequestMapping("/roles")
@RestController
class RoleController(val service: RoleService) {

    @PostMapping
    fun saveRole(@RequestBody role: RoleDTO) : ResponseEntity<Role> {
        var uri : URI = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/role").toUriString())
        return ResponseEntity.created(uri).body(service.create(role));
    }
}