package com.marjorie.scoop.config

import com.marjorie.scoop.auth.AuthenticationProviderService
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

/**
 * Manages configuration info regarding web authorization.
 */
@Configuration
class WebAuthorizationConfig(
    private val authenticationProvider: AuthenticationProviderService,
): WebSecurityConfigurerAdapter() {

    /**
     * Registers a custom authentication provider to Spring Security's authentication manager.
     */
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(authenticationProvider)
    }

    override fun configure(http: HttpSecurity) {
        http.formLogin().defaultSuccessUrl("/", true)
        http.authorizeRequests().anyRequest().authenticated()
    }
}