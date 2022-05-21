package com.marjorie.scoop.config

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class DummyUser(
    private val username: String,
    private val password: String
): UserDetails {
    
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return arrayListOf(
            SimpleGrantedAuthority("READ")
        )
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}