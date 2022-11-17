package upc.fib.pes.grup121.model

import javax.persistence.*

@Entity
@Table(name = "role")
class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column
    var userId: Long? = null,
    @Column
    var name : String? = null
) {
}