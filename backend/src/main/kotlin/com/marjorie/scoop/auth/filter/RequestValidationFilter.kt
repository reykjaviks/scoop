package com.marjorie.scoop.auth.filter

import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private const val REQUEST_ID_NAME = "REQUEST-ID"

/**
 * Used to filter requests that don't contain a request ID in the HTTP headers.
 */
class RequestValidationFilter: Filter {
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest: HttpServletRequest = request as HttpServletRequest
        val httpResponse: HttpServletResponse = response as HttpServletResponse
        val requestID = httpRequest.getHeader(REQUEST_ID_NAME)

        if (!requestID.isNullOrBlank())
            chain.doFilter(request, response)
        else
            httpResponse.status = HttpServletResponse.SC_BAD_REQUEST
    }
}