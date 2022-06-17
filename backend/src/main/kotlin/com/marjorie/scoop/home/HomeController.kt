package com.marjorie.scoop.home

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Landing point.
 */
@RestController
@RequestMapping("/")
class HomeController {
    @GetMapping
    fun home(): String = "Welcome to Scoop"

    @GetMapping("/auth")
    fun auth(): String {
        val auth: Authentication = SecurityContextHolder.getContext().authentication;
        return "Auth info: $auth"
    }
}