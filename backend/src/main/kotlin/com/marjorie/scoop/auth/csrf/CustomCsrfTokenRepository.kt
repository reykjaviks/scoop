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
        val csrfToken: Token = jpaTokenRepository.findTokenByIdentifier(identifier)
            ?: return null
        return DefaultCsrfToken(CSRF_HEADER, CSRF_PARAMETER, csrfToken.value)
    }

    override fun generateToken(request: HttpServletRequest): CsrfToken {
        logger.info("Generating a new token...")
        val uuid: String = UUID.randomUUID().toString()
        return DefaultCsrfToken(CSRF_HEADER, CSRF_PARAMETER, uuid)
    }

    override fun saveToken(expectedToken: CsrfToken?, request: HttpServletRequest, response: HttpServletResponse) {
        val identifier: String = request.getHeader(CSRF_IDENTIFIER)
        val savedToken: Token? = jpaTokenRepository.findTokenByIdentifier(identifier)

        if(shouldDeleteSavedToken(savedToken, expectedToken)) {
            logger.info("Deleting a saved token...")
            jpaTokenRepository.delete(savedToken!!)
        } else if(shouldSaveNewToken(savedToken, expectedToken)) {
            val newToken = Token(identifier = identifier, value = expectedToken!!.token)
            jpaTokenRepository.save(newToken)
        }
    }

    /**
     * Existing token located in the database needs to be deleted if the expected token is null. Old tokens are deleted
     * when an identifier makes a GET request after an application startup.
     */
    private fun shouldDeleteSavedToken(savedToken: Token?, expectedToken: CsrfToken?,): Boolean {
        return savedToken != null && expectedToken == null
    }

    private fun shouldSaveNewToken(savedToken: Token?, expectedToken: CsrfToken?): Boolean {
        return savedToken == null && expectedToken != null
    }
}