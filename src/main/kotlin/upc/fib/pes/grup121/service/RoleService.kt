package upc.fib.pes.grup121.service

import org.springframework.stereotype.Service
import upc.fib.pes.grup121.dto.User.RoleDTO
import upc.fib.pes.grup121.model.Role
import upc.fib.pes.grup121.repository.RoleRepository

@Service
class RoleService(var roleRepository: RoleRepository) {

    fun create(role: RoleDTO): Role {
        return roleRepository.save(Role.fromDto(role))
    }
}