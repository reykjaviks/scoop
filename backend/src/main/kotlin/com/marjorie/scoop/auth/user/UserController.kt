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

    @PostMapping
    fun createUser(@RequestBody registrationData: User) {
        logger.info {"Checking if username ${registrationData.username} already exists ..."}
        val usernameExists = userService.usernameExists(registrationData.username)

        if (!usernameExists)
            userService.createUserWithReadAuthority(registrationData)
        else throw
            ResponseStatusException(
                HttpStatus.CONFLICT,
                String.format("Username '%s' already exists.", registrationData.username)
            )
    }

    @GetMapping("/{username}")
    fun getUser(@PathVariable username: String): User {
        return userService.getUser(username)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                String.format("No user found for username '%s'", username)
            )
    }

}