package com.marjorie.scoop.auth.role

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * API for basic CRUD operations on 'Role'
 */
@Repository
interface RoleRepository: JpaRepository<Role?, Long?> {
    fun findByName(name: String): Role?
}
