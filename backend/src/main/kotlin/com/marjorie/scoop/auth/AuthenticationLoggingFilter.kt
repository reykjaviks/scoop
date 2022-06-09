package com.marjorie.scoop.auth

import mu.KotlinLogging
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

private val logger = KotlinLogging.logger {}

/**
 * Used to log request IDs after a successful authentication.
 */
class AuthenticationLoggingFilter: Filter {
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest: HttpServletRequest = request as HttpServletRequest
        val requestId = httpRequest.getHeader("request-id")
        logger.info("Successfully authenticated request with ID '$requestId'")
        chain.doFilter(request, response)
    }
}