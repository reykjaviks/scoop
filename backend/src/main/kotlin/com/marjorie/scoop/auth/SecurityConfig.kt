package com.marjorie.scoop.auth

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager

@Configuration
class SecurityConfig {
    val pwEncoder: PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    @Bean
    fun authentication(): UserDetailsService {
        val ella: UserDetails = User.builder()
            .username("Ella")
            .password(pwEncoder.encode("pass"))
            .roles(("ADMIN"))
            .build()

        val marja: UserDetails = User.builder()
            .username("Eliza")
            .password(pwEncoder.encode("pass"))
            .roles("USER", "ADMIN")
            .build()

        return InMemoryUserDetailsManager(ella, marja)
    }


}