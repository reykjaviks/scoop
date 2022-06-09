package com.marjorie.scoop.auth

import mu.KotlinLogging
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private val logger = KotlinLogging.logger {}

/**
 * Used to log request IDs after a successful authentication.
 */
class AuthenticationLoggingFilter: OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain)
    {
        val httpRequest: HttpServletRequest = request as HttpServletRequest
        val requestID = httpRequest.getHeader("request-id")
        logger.info("Successfully authenticated request with ID '$requestID'")
        filterChain.doFilter(request, response)
    }
}