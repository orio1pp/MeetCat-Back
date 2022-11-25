package upc.fib.pes.grup121.security.filters

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

import java.util.*
import java.util.stream.Collectors
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomAuthenticationFilter(
    val authenticationManager2: AuthenticationManager
) : UsernamePasswordAuthenticationFilter(
) {
    @Autowired
    override fun setAuthenticationManager(authenticationManager: AuthenticationManager?) {
        super.setAuthenticationManager(authenticationManager2)
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        var username: String = request.getParameter("username")
        var password: String = request.getParameter("password")
        var usernamePasswordAuthenticationToken: UsernamePasswordAuthenticationToken =
            UsernamePasswordAuthenticationToken(username, password)
        return authenticationManager2.authenticate(usernamePasswordAuthenticationToken)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse,
        chain: FilterChain?,
        authentication: Authentication
    ) {
        var user: User = authentication.principal as User
        var algorithm: Algorithm =
            Algorithm.HMAC256("secret".toByteArray()) //cambiar esto, la encriptacion habria que sacarla de algun sitio securizado.

        var access_token: String =
            JWT.create().withSubject(user.username).withExpiresAt(Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withIssuer(request?.requestURL.toString())
                .withClaim("roles", user.authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm)

        var refresh_token: String =
            JWT.create().withSubject(user.username).withExpiresAt(Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .withIssuer(request?.requestURL.toString())
                .sign(algorithm)

        var tokens : MutableMap<String, String> = mutableMapOf()
        tokens.put("access_token", access_token)
        tokens.put("refresh_token", refresh_token)
        response.contentType = APPLICATION_JSON_VALUE
        ObjectMapper().writeValue(response.outputStream, tokens)
    }
}