package com.marjorie.scoop.auth

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

/**
 * Takes care of the authentication logic, for example validates whether
 * passwords match when using credential based authentication.
 */
@Service
class AuthenticationProviderService(
    private val jpaUserDetailsService: JpaUserDetailsService,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder
): AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication {
        val username = authentication.name
        val user: SecurityUser = jpaUserDetailsService.loadUserByUsername(username)
        val rawPassword = authentication.credentials.toString()
        return this.checkPassword(user, rawPassword, bCryptPasswordEncoder)
    }

    override fun supports(authentication: Class<out Any>): Boolean {
        return UsernamePasswordAuthenticationToken::class.java.isAssignableFrom(authentication)
    }

    private fun checkPassword(user: SecurityUser, rawPassword: String, passwordEncoder: PasswordEncoder): Authentication {
        return if (passwordEncoder.matches(rawPassword, user.password)) {
            UsernamePasswordAuthenticationToken(
                user.username,
                user.password,
                user.authorities
            )
        } else throw BadCredentialsException("Bad credentials.") // info: unsafe to inform users that the username was correct but password was not
    }

}

