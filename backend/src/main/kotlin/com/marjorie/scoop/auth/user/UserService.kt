package com.marjorie.scoop.auth.user

import com.marjorie.scoop.auth.authority.AuthorityService
import com.marjorie.scoop.auth.userauthority.UserAuthorityService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Handles communication between User repository and User controller.
 */
@Service
class UserService(
    private val userRepository: UserRepository,
    private val authorityService: AuthorityService,
    private val userAuthorityService: UserAuthorityService,
    private val passwordEncoder: BCryptPasswordEncoder,
) {
    fun getUser(username: String): User? = userRepository.findByUsername(username)

    @Transactional
    fun createUserWithDefaultUserRole(registrationData: User) {
        val newUser = userRepository.save(
            User(
                name = registrationData.name,
                username = registrationData.username,
                password = this.passwordEncoder.encode(registrationData.password),
                authorities = null
            )
        )
        val authority = authorityService.findByName("ROLE_USER")
        if(authority != null) userAuthorityService.createUserAuthorityConnection(newUser, authority)
        else throw KotlinNullPointerException("Cannot create an User-Authority connection because authority is null.")
    }

    fun usernameExists(username: String): Boolean {
        return userRepository.existsByUsername(username)
    }
}