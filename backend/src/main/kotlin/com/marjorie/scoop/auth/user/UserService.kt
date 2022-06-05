package com.marjorie.scoop.auth.user

import com.marjorie.scoop.auth.authority.Authority
import com.marjorie.scoop.auth.authority.AuthorityRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

/**
 * Handles communication between User repository and User controller.
 */
@Service
class UserService(
    private val userRepository: UserRepository,
    private val authorityRepository: AuthorityRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
) {
    fun getUser(username: String): User? = userRepository.findByUsername(username)

    fun createUserWithReadAuthority(registrationData: User) {
        val newUser = userRepository.save(
            User(
                name = registrationData.name,
                username = registrationData.username,
                password = this.passwordEncoder.encode(registrationData.password),
                authorities = null
            )
        )
        authorityRepository.save(
            Authority(
                name = "READ",
                user = newUser
            )
        )
    }

    fun usernameExists(username: String): Boolean {
        return userRepository.existsByUsername(username)
    }

}