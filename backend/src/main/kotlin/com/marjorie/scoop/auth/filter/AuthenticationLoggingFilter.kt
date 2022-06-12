package com.marjorie.scoop.auth.filter

import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private const val REQUEST_ID_NAME = "REQUEST-ID"

/**
 * Used to log request IDs after a successful authentication.
 */
class AuthenticationLoggingFilter: OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val requestID = request.getHeader(REQUEST_ID_NAME)
        logger.info("Successfully authenticated a request with REQUEST-ID: $requestID")
        chain.doFilter(request, response)
    }
}