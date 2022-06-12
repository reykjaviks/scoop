package com.marjorie.scoop.auth.filter

import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Used to log request IDs after a successful authentication.
 */
class AuthenticationLoggingFilter: OncePerRequestFilter() {
    override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, filterChain: FilterChain) {
        val httpRequest: HttpServletRequest = req as HttpServletRequest
        val requestID = httpRequest.getHeader("request-id")
        logger.info("Successfully authenticated a request with ID '$requestID'")
        filterChain.doFilter(req, res)
    }
}