package com.marjorie.scoop.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.provisioning.JdbcUserDetailsManager
import javax.sql.DataSource

/**
 * Manages configuration info regarding user management.
 */
@Configuration
class UserManagementConfig {
    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder? {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun userDetailsService(dataSource: DataSource?): UserDetailsService? {
        val authsByUserQuery = """
            select 
                u.username, 
                a.name as authority
            from scoop.user u
            inner join userauthority ua on ua.user_id = u.id
            inner join authority a on a.id = ua.authority_id
            where u.username = ?
        """

        return JdbcUserDetailsManager(dataSource).also {
            it.setAuthoritiesByUsernameQuery(authsByUserQuery)
        }
    }
}