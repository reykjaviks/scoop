package com.marjorie.scoop.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager

@Configuration
class SecurityConfig: WebSecurityConfigurerAdapter() {
    val pwEncoder: PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    @Bean
    fun authentication(): UserDetailsService {
        val ella: UserDetails = User.builder()
            .username("Ella")
            .password(pwEncoder.encode("pass"))
            .roles(("USER"))
            .build()

        val marja: UserDetails = User.builder()
            .username("Eliza")
            .password(pwEncoder.encode("pass"))
            .roles("USER", "ADMIN")
            .build()

        return InMemoryUserDetailsManager(ella, marja)
    }

    override fun configure(http: HttpSecurity) {
        http.httpBasic()
        http.authorizeRequests()
            .anyRequest().authenticated()
    }

}
