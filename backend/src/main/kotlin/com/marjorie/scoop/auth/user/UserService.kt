package com.marjorie.scoop.auth.user

import com.marjorie.scoop.auth.role.RoleRepository
import com.marjorie.scoop.auth.userrole.UserRole
import com.marjorie.scoop.auth.userrole.UserRoleRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Handles communication between User repository and User controller.
 */
@Service
class UserService(
    private val userRepository: UserRepository,
    private val userRoleRepository: UserRoleRepository,
    private val roleRepository: RoleRepository,
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
                roles = null,
                authorities = null
            )
        )
        this.createUserRoleConnection(newUser, "USER")
    }

    private fun createUserRoleConnection(user: User, roleName: String): UserRole {
        val role = roleRepository.findByName(roleName)
        return if(role != null)
            userRoleRepository.save(
                UserRole(
                    user = user,
                    role = role
                )
            )
        else throw NullPointerException("Role '$roleName' does not exist.")
    }

    fun usernameExists(username: String): Boolean {
        return userRepository.existsByUsername(username)
    }

}