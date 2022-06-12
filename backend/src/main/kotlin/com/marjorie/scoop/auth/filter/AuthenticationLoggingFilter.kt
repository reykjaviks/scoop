package com.marjorie.scoop.auth.filter

import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Used to log request IDs after a successful authentication.
 */
class AuthenticationLoggingFilter: OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val requestID = request.getHeader("request-id")
        logger.info("Successfully authenticated a request with ID '$requestID'")
        chain.doFilter(request, response)
    }
}