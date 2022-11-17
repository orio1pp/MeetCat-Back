package upc.fib.pes.grup121.model

import javax.persistence.*

@Entity
@Table(name = "user")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column
    var userId: Long? = null,

    @Column
    private var username: String? = null,

    @Column
    private var password: String? = null,

    @Column
    private var mail: String? = null,

    @ManyToMany(fetch = FetchType.EAGER)
    private var roles: MutableCollection<Role> = mutableListOf<Role>()
) {
    fun getRoles(): MutableCollection<Role>{
        return roles;
    }
    fun getUsername(): String?{
        return username;
    }
    fun getPassword(): String?{
        return username;
    }

    fun setPassword(password: String?){
        this.password = password;
    }
    
}