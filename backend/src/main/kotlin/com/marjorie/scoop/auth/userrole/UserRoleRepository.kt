package com.marjorie.scoop.auth.userrole

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * API for basic CRUD operations on 'UserRole'
 */
@Repository
interface UserRoleRepository: JpaRepository<UserRole?, Long?>