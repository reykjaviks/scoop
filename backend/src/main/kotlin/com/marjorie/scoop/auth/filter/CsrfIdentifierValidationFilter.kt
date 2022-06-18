package com.marjorie.scoop.auth.filter

import com.marjorie.scoop.common.Constants.CSRF_IDENTIFIER
import mu.KotlinLogging
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private val logger = KotlinLogging.logger {}

/**
 * Intercepts requests that don't contain a CSRF identifier in the HTTP headers.
 */
class CsrfIdentifierValidationFilter: Filter {
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest: HttpServletRequest = request as HttpServletRequest
        val httpResponse: HttpServletResponse = response as HttpServletResponse
        val requestID = request.getHeader(CSRF_IDENTIFIER)
        if (requestID.isNullOrBlank()) {
            logger.info("Request intercepted because $CSRF_IDENTIFIER is null or blank.")
            httpResponse.status = HttpServletResponse.SC_BAD_REQUEST
        } else {
            chain.doFilter(httpRequest, httpResponse)
        }
    }
}