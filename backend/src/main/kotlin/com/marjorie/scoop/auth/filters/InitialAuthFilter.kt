package com.marjorie.scoop.auth.filters

import com.marjorie.scoop.auth.UsernamePasswordAuth
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Lazy
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.nio.charset.StandardCharsets
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

// todo: add exceptions and logging
/**
 * First step in the authentication process.
 */
@Component
class InitialAuthFilter(@Lazy private val authManager: AuthenticationManager): OncePerRequestFilter() {

    @Value("\${jwt.signing.key}")
    private lateinit var signingKey: String

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val loginUsername = request.getHeader("username")
        val loginPassword = request.getHeader("password")
        val auth = authManager.authenticate(UsernamePasswordAuth(loginUsername, loginPassword))

        val key = Keys.hmacShaKeyFor(signingKey.toByteArray(StandardCharsets.UTF_8))
        val claims: Claims = Jwts.claims()
        claims["username"] = loginUsername
        claims["authorities"] = auth.authorities

        val jwt: String = Jwts.builder()
            .setClaims(claims)
            .signWith(key)
            .compact()

        response.setHeader("Authorization", jwt);
    }

    /**
     * Filter is applied only on the login-path.
     */
    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return !request.servletPath.equals("/login")
    }

}