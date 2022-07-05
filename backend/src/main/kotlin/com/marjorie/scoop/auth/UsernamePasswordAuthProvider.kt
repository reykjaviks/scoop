package com.marjorie.scoop.auth

import com.marjorie.scoop.auth.user.JpaUserDetailsService
import com.marjorie.scoop.auth.user.SecurityUser
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

/**
 * Takes care of authentication logic when using a username and password based authentication method.
 */
@Service
class UsernamePasswordAuthProvider(
    private val jpaUserDetailsService: JpaUserDetailsService,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
): AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication {
        val loginUsername = authentication.name
        val loginPassword = authentication.credentials.toString()

        val user: SecurityUser = jpaUserDetailsService.loadUserByUsername(loginUsername)
        return this.checkPassword(user, loginPassword, bCryptPasswordEncoder)
    }

    override fun supports(authentication: Class<out Any>): Boolean {
        return UsernamePasswordAuthenticationToken::class.java.isAssignableFrom(authentication)
    }

    private fun checkPassword(user: SecurityUser, loginPassword: String, passwordEncoder: PasswordEncoder): Authentication {
        return if (passwordEncoder.matches(loginPassword, user.password)) {
            UsernamePasswordAuthenticationToken(
                user.username,
                user.password,
                user.authorities
            )
        } else throw BadCredentialsException("Bad credentials.")
    }

}

