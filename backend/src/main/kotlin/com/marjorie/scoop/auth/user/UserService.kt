package com.marjorie.scoop.auth.user

import com.marjorie.scoop.auth.authority.AuthorityService
import com.marjorie.scoop.auth.userauthority.UserAuthorityService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Handles communication between the user repository and user controller.
 */
@Service
class UserService(
    private val userRepository: UserRepository,
    private val authorityService: AuthorityService,
    private val userAuthorityService: UserAuthorityService,
    private val passwordEncoder: BCryptPasswordEncoder,
) {
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or #username == authentication.name")
    fun getUser(username: String): UserEntity? = userRepository.findByUsername(username)

    @Transactional
    fun createUserWithDefaultUserRole(registrationData: UserEntity) {
        val newUser = userRepository.save(
            UserEntity(
                name = registrationData.name,
                username = registrationData.username,
                password = this.passwordEncoder.encode(registrationData.password),
                authorities = null,
                reviewList = null,
            )
        )
        val authority = authorityService.findByName("ROLE_USER")
        if(authority != null)
            userAuthorityService.createUserAuthorityConnection(newUser, authority)
        else
            throw KotlinNullPointerException("Can't save userAuthority to the database because authority is null.")
    }

    fun usernameExists(username: String): Boolean {
        return userRepository.existsByUsername(username)
    }
}