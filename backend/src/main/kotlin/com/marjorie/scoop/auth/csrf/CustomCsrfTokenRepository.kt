package com.marjorie.scoop.auth.csrf

import com.marjorie.scoop.common.Constants.CSRF_HEADER
import com.marjorie.scoop.common.Constants.CSRF_IDENTIFIER
import com.marjorie.scoop.common.Constants.CSRF_PARAMETER
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.security.web.csrf.CsrfTokenRepository
import org.springframework.security.web.csrf.DefaultCsrfToken
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private val logger = KotlinLogging.logger {}

/**
 * Uses an injected instance of JpaTokenRepository to retrieve and save CSRF tokens in the database.
 */
class CustomCsrfTokenRepository: CsrfTokenRepository {
    @Autowired
    private lateinit var jpaTokenRepository: JpaTokenRepository

    override fun loadToken(request: HttpServletRequest): CsrfToken? {
        val identifier: String = request.getHeader(CSRF_IDENTIFIER)
        val existingToken: Token? = jpaTokenRepository.findTokenByIdentifier(identifier)

        if(existingToken == null) {
            logger.info("Identifier $identifier doesn't have an existing CSRF token")
            return null
        }
        logger.info("Loading an existing CSRF token associated with the identifier '$identifier'")
        return DefaultCsrfToken(CSRF_HEADER, CSRF_PARAMETER, existingToken.value)

    }

    override fun generateToken(request: HttpServletRequest): CsrfToken {
        logger.info("Generating a new token...")
        val uuid: String = UUID.randomUUID().toString()
        return DefaultCsrfToken(CSRF_HEADER, CSRF_PARAMETER, uuid)
    }

    override fun saveToken(csrfToken: CsrfToken?, request: HttpServletRequest, response: HttpServletResponse) {
        val identifier: String = request.getHeader(CSRF_IDENTIFIER)
        val existingToken: Token? = jpaTokenRepository.findTokenByIdentifier(identifier)

        if(csrfToken == null) {
            logger.info("Removing a CSRF token associated with the request from the session...")
            request.getSession(false)?.removeAttribute(CSRF_HEADER)
        } else if (existingToken == null) {
            logger.info("Saving a new token to identifier $identifier into the database")
            val newToken = Token(identifier = identifier, value = csrfToken.token)
            jpaTokenRepository.save(newToken)
        }
    }
}