package com.marjorie.scoop.auth.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * API for basic CRUD operations on 'User'
 */
@Repository
interface UserRepository: JpaRepository<User?, Long?> {
    fun findByUsername(username: String): User?
    fun existsByUsername(username: String): Boolean
}