package upc.fib.pes.grup121.security.filters

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomAuthorizationFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (request.servletPath.equals("/login") || request.servletPath.equals("/refresh/token")) {
            filterChain.doFilter(request, response)
        } else {
            var authorizationHeader: String? = request.getHeader(AUTHORIZATION)
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                try {
                    var token: String = authorizationHeader.substring("Bearer ".length)
                    var algorithm: Algorithm = Algorithm.HMAC256("secret".toByteArray())
                    var verifier: JWTVerifier = JWT.require(algorithm).build()
                    var decodedJWT: DecodedJWT = verifier.verify(token)
                    var username: String = decodedJWT.subject
                    var roles = decodedJWT.getClaim("roles").asArray(String().javaClass)
                    var authorities: MutableCollection<SimpleGrantedAuthority> = mutableListOf()
                    if (roles != null) {
                        for (role: String in roles) {
                            if (role != null)
                                authorities.add(SimpleGrantedAuthority(role.toString()))
                        }
                    }
                    var authenticationToken = UsernamePasswordAuthenticationToken(username, null, authorities)
                    SecurityContextHolder.getContext().authentication = authenticationToken
                    filterChain.doFilter(request, response)
                } catch (ex: Exception) {
                    response.setHeader("error", ex.message)
                    response.status = 403 //forbidden code
                    var error: MutableMap<String, String> = mutableMapOf()
                    ex.message?.let { error.put("error", it) }
                    response.contentType = APPLICATION_JSON_VALUE
                    ObjectMapper().writeValue(response.outputStream, error)
                }
            } else {
                filterChain.doFilter(request, response)
            }
        }
    }
}