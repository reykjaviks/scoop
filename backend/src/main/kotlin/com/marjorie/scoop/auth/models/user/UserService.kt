package com.marjorie.scoop.auth.models.user

import com.marjorie.scoop.auth.models.authority.AuthorityService
import com.marjorie.scoop.auth.models.user.dto.UserDTO
import com.marjorie.scoop.auth.models.user.dto.UserPostDTO
import com.marjorie.scoop.auth.models.userauthority.UserAuthorityService
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
    private val userMapper: UserMapper
) {
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or #username == authentication.name")
    fun getUserDTO(username: String): UserDTO? {
        val userEntity = userRepository.findByUsername(username) ?: return null
        return userMapper.mapToUserDTO(userEntity)
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or #username == authentication.name")
    fun getUserEntity(username: String): UserEntity? {
        return userRepository.findByUsername(username)
    }

    @Transactional
    fun createUserWithDefaultUserRole(applicant: UserPostDTO) {
        val newUser = userRepository.save(
            UserEntity(
                name = applicant.name,
                username = applicant.username.lowercase(),
                password = this.passwordEncoder.encode(applicant.password),
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