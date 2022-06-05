package com.marjorie.scoop.auth

import com.marjorie.scoop.auth.authority.Authority
import com.marjorie.scoop.auth.user.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors

/**
 * Wraps the User entity inside SecurityUser in order to implement's Spring Security's UserDetails interface.
 */
class SecurityUser(private val user: User): UserDetails {
    override fun getUsername(): String {
        return user.username
    }

    override fun getPassword(): String {
        return user.password
    }

    override fun getAuthorities(): MutableList<SimpleGrantedAuthority>? {
        return user.authorities?.stream()
            ?.map { a: Authority -> SimpleGrantedAuthority(a.name) }
            ?.collect(Collectors.toList())
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