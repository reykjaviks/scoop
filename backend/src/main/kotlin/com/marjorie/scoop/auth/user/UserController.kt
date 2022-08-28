package com.marjorie.scoop.auth.user

import com.marjorie.scoop.auth.user.dto.UserAuthDTO
import com.marjorie.scoop.auth.user.dto.UserDTO
import com.marjorie.scoop.auth.user.dto.UserPostDTO
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

private val logger = KotlinLogging.logger {}

/**
 * Exposes endpoints that serve information on users.
 */
@RestController
@RequestMapping("/api/user")
class UserController(private val userService: UserService) {
    @GetMapping("/{username}")
    fun getUser(@PathVariable username: String): UserDTO? {
        return userService.getUserDTO(username)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "No user found for username $username")
    }

    @PostMapping("/add")
    fun createUser(@RequestBody applicant: UserPostDTO) {
        val usernameExists: Boolean = userService.usernameExists(applicant.username.lowercase()) // usernames need to be checked in a lower case format
        if (usernameExists) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "Username ${applicant.username.lowercase()} already exists.")
        } else {
            logger.info("Creating a new user '${applicant.username}'...")
            userService.createUserWithDefaultUserRole(applicant)
        }
    }
}