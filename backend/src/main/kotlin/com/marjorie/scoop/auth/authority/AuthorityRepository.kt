package com.marjorie.scoop.auth.authority

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * API for basic CRUD operations on 'Authority'
 */
@Repository
interface AuthorityRepository: JpaRepository<Authority?, Long?> {
    fun findByName(name: String): Authority?
}
