package com.marjorie.scoop.auth.filters

import com.marjorie.scoop.common.Constants.REQUEST_ID
import mu.KotlinLogging
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST

private val logger = KotlinLogging.logger {}

/**
 * Used to filter requests that don't contain a request ID in the HTTP headers.
 */
class RequestValidationFilter: Filter {
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest: HttpServletRequest = request as HttpServletRequest
        val httpResponse: HttpServletResponse = response as HttpServletResponse
        val requestID = request.getHeader(REQUEST_ID)
        if (requestID.isNullOrBlank()) {
            logger.info("Request intercepted because $REQUEST_ID is null or blank.")
            httpResponse.status = SC_BAD_REQUEST
        } else {
            chain.doFilter(httpRequest, httpResponse)
        }
    }
}