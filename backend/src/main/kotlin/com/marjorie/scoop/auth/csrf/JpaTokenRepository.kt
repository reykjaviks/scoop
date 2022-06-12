package com.marjorie.scoop.auth.csrf

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * API for basic CRUD operations on Token
 */
@Repository
interface JpaTokenRepository: JpaRepository<Token?, Long?> {
    fun findTokenByIdentifier(identifier: String): Token?
}