package com.marjorie.scoop.user

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

/**
 * Takes care of authentication logic, for example validates whether
 * passwords match when using credential based authentication.
 */
@Component
class CustomAuthenticationProvider(
    private val userDetailsService: UserDetailsService,
    private val passwordEncoder: PasswordEncoder,
): AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication {
        val username = authentication.name
        val password = authentication.credentials.toString()

        val userDetails: UserDetails = userDetailsService.loadUserByUsername(username)

        return if (this.passwordEncoder.matches(password, userDetails.password)) {
            UsernamePasswordAuthenticationToken(username, password, userDetails.authorities)
        } else throw BadCredentialsException("Incorrect password.") // info: unsafe to inform users that the username was correct but password was not
    }

    override fun supports(authentication: Class<out Any>): Boolean {
        return UsernamePasswordAuthenticationToken::class.java.isAssignableFrom(authentication)
    }

}

