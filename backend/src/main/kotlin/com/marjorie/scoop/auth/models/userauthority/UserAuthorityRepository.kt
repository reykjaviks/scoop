package com.marjorie.scoop.auth.models.userauthority

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * API for basic CRUD operations on 'UserRole'
 */
@Repository
interface UserAuthorityRepository: JpaRepository<UserAuthority?, Long?>