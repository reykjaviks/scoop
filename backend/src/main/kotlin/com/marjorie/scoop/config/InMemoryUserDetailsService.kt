package com.marjorie.scoop.config

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService

class InMemoryUserDetailsService(private val users: List<UserDetails>): UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        return this.users.first { it.username.equals(username) }
    }
}