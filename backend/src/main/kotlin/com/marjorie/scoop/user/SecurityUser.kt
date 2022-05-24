package com.marjorie.scoop.user

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * Wraps the User entity inside SecurityUser in order to implement's Spring Security's
 * UserDetails interface.
 */
class SecurityUser(private val user: User): UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return arrayListOf(
            SimpleGrantedAuthority(user.authority)
        )
    }

    override fun getPassword(): String {
        return user.password
    }

    override fun getUsername(): String {
        return user.username
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