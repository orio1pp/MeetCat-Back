package upc.fib.pes.grup121.Configuration

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.streams.toList
@Component
class CustomAuthenticationFilter(
    private final var authenticationManager: AuthenticationManager
) : UsernamePasswordAuthenticationFilter(
) {
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        var verifier: GoogleIdTokenVerifier = GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory()).build()
        var googleToken: String = request.getParameter("token");
        var idToken: GoogleIdToken = verifier.verify(googleToken);
        idToken.let {
            var payload: GoogleIdToken.Payload? = it.payload;
            var idToken: String = request.getParameter("id"); 
            var usernamePasswordAuthenticationToken: UsernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(payload?.email,idToken);
            return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        }
        throw Exception("error when trying to authenticate");
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse,
        chain: FilterChain?,
        authentication: Authentication
    ) {
        var user: User = authentication.principal as User
        var algorithm : Algorithm = Algorithm.HMAC256("secret".toByteArray()) //cambiar esto, la encriptacion habria que sacarla de algun sitio securizado.
        var access_token : String = JWT.create().withSubject(user.username).withExpiresAt(Date(System.currentTimeMillis() + 30*60*1000))
            .withIssuer(request?.requestURL.toString())
            .withClaim("roles", user.authorities.stream().map(GrantedAuthority::getAuthority).toList())
            .sign(algorithm)
        var refresh_token : String = JWT.create().withSubject(user.username).withExpiresAt(Date(System.currentTimeMillis() + 30*60*1000))
            .withIssuer(request?.requestURL.toString())
            .sign(algorithm)
        response.setHeader("accest_token", access_token);
        response.setHeader("refresh_token", refresh_token);
    }
}