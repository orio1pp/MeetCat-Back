package upc.fib.pes.grup121.model

import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*

@Entity
@DynamicUpdate
@Table(name = "llocs")
class Lloc (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) var id: Long?,
    var name: String?,
    var location: String?, // guardar cordenades com string
    var address: String?
    )
{}