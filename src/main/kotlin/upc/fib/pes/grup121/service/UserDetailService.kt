package upc.fib.pes.grup121.service
import org.springframework.context.annotation.Bean
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import upc.fib.pes.grup121.model.Role
import upc.fib.pes.grup121.model.User
import upc.fib.pes.grup121.model.UserService
import upc.fib.pes.grup121.repository.RoleRepository
import upc.fib.pes.grup121.repository.UserRepository
import javax.transaction.Transactional

@Service
@Transactional
class UserDetailService(
    private final var userRepository: UserRepository,
    private final var roleRepository: RoleRepository
): UserService, UserDetailsService {
    private var passwordEncoder: PasswordEncoder = BCryptPasswordEncoder()
    override fun loadUserByUsername(username: String): UserDetails? {
        username.let {
            var user: User = userRepository.findByUsername(it);
            var authorities: MutableCollection<SimpleGrantedAuthority> = mutableListOf()
            var roles: MutableCollection<Role> = user.getRoles()
            for(role in roles){
                authorities.add(SimpleGrantedAuthority(role.name));
            }
            return org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),authorities);
        }

        return null;
    }

    override fun saveUser(user: User): User {
        user.setPassword(passwordEncoder.encode(user.getPassword()))
        return userRepository.save(user);
    }

    override fun saveRole(role: Role): Role {
        return roleRepository.save(role);
    }

    override fun addRoleToUser(username: String, roleName: String) {
        var role: Role = roleRepository.findByName(roleName);
        var user: User = userRepository.findByUsername(username);
        user.getRoles().add(role)
    }

    override fun getUser(username: String): User {
        return userRepository.findByUsername(username);
    }

    override fun getUsers(): List<User> {
        return userRepository.findAll();
    }
}