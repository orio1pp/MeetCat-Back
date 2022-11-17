package upc.fib.pes.grup121.Configuration

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


@Configuration
@EnableWebSecurity
class SecurityConfig(private final var userDetaislService: UserDetailsService
) : WebSecurityConfigurerAdapter() {
    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(userDetaislService)?.passwordEncoder(BCryptPasswordEncoder())
        var a : AuthenticationManagerBuilder? =auth;
    }

    override fun configure(http: HttpSecurity) {
        var customAuthenticationFilter:CustomAuthenticationFilter = CustomAuthenticationFilter(authenticationManagerBean())
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        http.authorizeHttpRequests().anyRequest().permitAll();
        http.addFilter(customAuthenticationFilter);
    }

    @Bean
    override fun authenticationManagerBean():AuthenticationManager{
        return super.authenticationManagerBean();
    }


}