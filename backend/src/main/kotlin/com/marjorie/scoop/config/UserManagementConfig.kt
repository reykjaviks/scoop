package com.marjorie.scoop.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.JdbcUserDetailsManager
import javax.sql.DataSource

/**
 * Manages configuration info regarding users.
 */
@Configuration
class UserManagementConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return NoOpPasswordEncoder.getInstance()
    }

    @Bean
    fun userDetailsService(dataSource: DataSource?): UserDetailsService? {
        val usersByUsernameQuery = "select email, password, isenabled from scoop.appuser where email = ?"
        val authsByUserQuery = "select email, authority from scoop.appuser where email = ?"

        return JdbcUserDetailsManager(dataSource).also {
            it.usersByUsernameQuery = usersByUsernameQuery
            it.setAuthoritiesByUsernameQuery(authsByUserQuery)
        }
    }

    /*
    @Bean
    fun userDetailsService(): UserDetailsService {
        val user1: UserDetails = SecurityUser(
            User("marja@gmail.com", "password", "read")
        )
        val users: List<UserDetails> = listOf(user1)
        return InMemoryUserDetailsService(users)
    }


    @Bean
    fun authentication(): UserDetailsService {
        val ella: UserDetails = User.builder()
            .username("Ella")
            .password(pwEncoder.encode("pass"))
            .roles(("USER"))
            .build()

        val marja: UserDetails = User.builder()
            .username("Eliza")
            .password(pwEncoder.encode("pass"))
            .roles("USER", "ADMIN")
            .build()

        return InMemoryUserDetailsManager(ella, marja)
    }
     */

}
