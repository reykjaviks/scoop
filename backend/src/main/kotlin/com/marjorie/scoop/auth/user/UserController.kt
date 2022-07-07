package com.marjorie.scoop.auth.user

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
    fun getUser(@PathVariable username: String): UserEntity? {
        return userService.getUser(username)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "No user found for username $username")
    }

    @PostMapping("/add")
    fun createUser(@RequestBody registrationData: UserEntity) {
        val usernameExists: Boolean = userService.usernameExists(registrationData.username)
        if (usernameExists) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "Username ${registrationData.username} already exists.")
        } else {
            logger.info("Creating a new user '${registrationData.username}'...")
            userService.createUserWithDefaultUserRole(registrationData)
        }
    }
}