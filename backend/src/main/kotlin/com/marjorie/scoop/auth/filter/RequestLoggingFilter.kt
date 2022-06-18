package com.marjorie.scoop.auth.filter

import com.marjorie.scoop.common.Constants.REQUEST_ID
import mu.KotlinLogging
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private val kotlinLogger = KotlinLogging.logger {}

/**
 * Used to log request IDs after a successful authentication.
 */
class RequestLoggingFilter: OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val requestID = request.getHeader(REQUEST_ID)
        kotlinLogger.info("Successfully authenticated a request with an ID '$requestID'")
        chain.doFilter(request, response)
    }
}