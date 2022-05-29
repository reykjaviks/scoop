package com.marjorie.scoop.user

import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


/**
 * Handles communication between the User repository and Authentication Provider.
 */
@Service
class JpaUserDetailsService(
    private val userRepository: UserRepository,
): UserDetailsService {
    override fun loadUserByUsername(username: String): SecurityUser {
        val user = userRepository.findByUsername(username) ?: throw UsernameNotFoundException("Username not found.")
        return SecurityUser(user)
      }
}