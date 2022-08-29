package com.marjorie.scoop.auth.models.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * API for basic CRUD operations on 'User'
 */
@Repository
interface UserRepository: JpaRepository<UserEntity?, Long?> {
    fun findByUsername(username: String): UserEntity?
    fun existsByUsername(username: String): Boolean
}