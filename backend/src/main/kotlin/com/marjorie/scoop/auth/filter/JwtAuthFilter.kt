package com.marjorie.scoop.auth.filter

import com.marjorie.scoop.auth.UsernamePasswordAuth
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.nio.charset.StandardCharsets
import javax.crypto.SecretKey
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Expects requests to have a JWT token in the Authorization header. Validates the JTW token
 * by checking the signature and creates an authenticated Authentication object. Also adds the Authentication object
 * into the SecurityContext.
 */
@Component
class JwtAuthFilter: OncePerRequestFilter() {
    @Value("\${jwt.signing.key}")
    private lateinit var signingKey: String

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val jwt = request.getHeader("Authorization")
        val key: SecretKey = Keys.hmacShaKeyFor(signingKey.toByteArray(StandardCharsets.UTF_8))
        val claims: Claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(jwt)
            .body

        val username = claims["username"].toString()
        val authority: GrantedAuthority = SimpleGrantedAuthority("user")
        val auth = UsernamePasswordAuth(username, null, listOf(authority))

        SecurityContextHolder.getContext().authentication = auth
        filterChain.doFilter(request, response)
    }

    /**
     * Filter is not being triggered on the login path.
     */
    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val paths = arrayOf<String>("/login", "")
        return request.servletPath.equals("/login")
    }

}