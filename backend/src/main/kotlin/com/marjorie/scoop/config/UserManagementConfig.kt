package com.marjorie.scoop.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager

/**
 * User builder for simple applications.
 */
@Configuration
class UserManagementConfig {
    //val pwEncoder: PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return NoOpPasswordEncoder.getInstance()
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        val user1: UserDetails = SecurityUser(
            User("ella@gmail.com", "password", "read")
        )
        val users: List<UserDetails> = listOf(user1)
        return InMemoryUserDetailsService(users)
    }

    /*
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
     */

}
