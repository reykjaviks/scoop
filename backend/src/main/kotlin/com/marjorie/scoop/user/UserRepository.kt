package com.marjorie.scoop.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User?, Long?> {
    fun findByUsername(email: String): User?
    fun existsByUsername(email: String): Boolean
}