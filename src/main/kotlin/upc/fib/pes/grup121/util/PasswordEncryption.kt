package upc.fib.pes.grup121.util

import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate
import java.security.MessageDigest
import kotlin.random.Random

class PasswordEncryption {
    val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    fun generateSalt() : String = (1..30)
        .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
        .joinToString("")

    fun hashString(input : String) : String {
        val HEX_CHARS = "0123456789ABCDEF"
        val bytes = MessageDigest
            .getInstance("SHA-256")
            .digest(input.toByteArray())
        val result = StringBuilder(bytes.size * 2)

        bytes.forEach {
            val i = it.toInt()
            result.append(HEX_CHARS[i shr 4 and 0x0f])
            result.append(HEX_CHARS[i and 0x0f])
        }
        return result.toString()
    }
}