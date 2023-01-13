package upc.fib.pes.grup121.security

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import upc.fib.pes.grup121.security.filters.CustomAuthenticationFilter
import upc.fib.pes.grup121.security.filters.CustomAuthorizationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    @Qualifier("userService") val userDetaislService: UserDetailsService,
) : WebSecurityConfigurerAdapter() {

    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetaislService).passwordEncoder(passwordEncoder())
    }

    override fun configure(http: HttpSecurity) {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        http.authorizeRequests().antMatchers("/login", "/users/refresh/token", "/**").permitAll()
        //http.authorizeRequests().antMatchers("/events/reported").hasRole("admin")
        //http.authorizeRequests().antMatchers("/events/{id}/unreport").hasRole("admin")
        http.authorizeRequests().anyRequest().authenticated()
        http.addFilter(CustomAuthenticationFilter(authenticationManagerBean()))
        http.addFilterBefore(CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter().javaClass)
    }

    @Bean
    override fun authenticationManagerBean() : AuthenticationManager {
        return super.authenticationManagerBean()
    }
}