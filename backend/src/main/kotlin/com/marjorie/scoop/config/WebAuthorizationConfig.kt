package com.marjorie.scoop.config

import com.marjorie.scoop.auth.UsernamePasswordAuthProvider
import com.marjorie.scoop.auth.csrf.CustomCsrfTokenRepository
import com.marjorie.scoop.auth.filter.CsrfIdentifierValidationFilter
import com.marjorie.scoop.auth.filter.CsrfLoggingFilter
import com.marjorie.scoop.auth.filter.RequestLoggingFilter
import com.marjorie.scoop.auth.filter.RequestValidationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.web.csrf.CsrfFilter
import org.springframework.security.web.csrf.CsrfTokenRepository

/**
 * Manages configuration info regarding web authorization.
 */
@Configuration
class WebAuthorizationConfig(
    private val usernamePasswordAuthProvider: UsernamePasswordAuthProvider,
): WebSecurityConfigurerAdapter() {
    @Bean
    fun csrfTokenRepository(): CsrfTokenRepository {
        return CustomCsrfTokenRepository()
    }

    /**
     * Registers a custom authentication provider to Spring Security's authentication manager.
     */
    override fun configure(authManagerBuilder: AuthenticationManagerBuilder) {
         authManagerBuilder.authenticationProvider(usernamePasswordAuthProvider)
    }

    override fun configure(httpSecurity: HttpSecurity) {
        // General
        httpSecurity.httpBasic()
        httpSecurity.formLogin().defaultSuccessUrl("/auth", true)
        httpSecurity.csrf { csrfConfigurer: CsrfConfigurer<HttpSecurity?> ->
            csrfConfigurer.csrfTokenRepository(csrfTokenRepository())
        }

        // Filters
        httpSecurity.addFilterBefore(
            CsrfIdentifierValidationFilter(),
            CsrfFilter::class.java
        ).addFilterAfter(
            CsrfLoggingFilter(),
            CsrfFilter::class.java
        ).addFilterBefore(
            RequestValidationFilter(),
            BasicAuthenticationFilter::class.java
        ).addFilterAfter(
            RequestLoggingFilter(),
            BasicAuthenticationFilter::class.java
        )

        // Endpoints
        httpSecurity.authorizeRequests()
            .mvcMatchers(HttpMethod.GET, "/").permitAll()
            .and()
         .authorizeRequests()
            .mvcMatchers(HttpMethod.GET, "/auth").authenticated()
            .and()
        .authorizeRequests()
            .mvcMatchers(HttpMethod.GET, "/api/user/*").hasAuthority("ROLE_ADMIN")
            .mvcMatchers(HttpMethod.POST, "/api/user/add").authenticated()
            .mvcMatchers(HttpMethod.DELETE, "/api/user/*").authenticated()
            .and()
        .authorizeRequests()
            .mvcMatchers(HttpMethod.GET, "/api/venue/*").permitAll()
            .mvcMatchers(HttpMethod.POST, "/api/venue/add").authenticated()
            .mvcMatchers(HttpMethod.DELETE, "/api/venue/*").authenticated()
            .and()
        .authorizeRequests()
            .mvcMatchers(HttpMethod.GET, "/api/review/*").permitAll()
            .mvcMatchers(HttpMethod.POST, "/api/review/add").authenticated()
            .mvcMatchers(HttpMethod.DELETE, "/api/review/*").authenticated()
    }
}