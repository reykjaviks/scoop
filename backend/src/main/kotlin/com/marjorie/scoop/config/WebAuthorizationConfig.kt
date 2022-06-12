package com.marjorie.scoop.config

import com.marjorie.scoop.auth.filter.AuthenticationLoggingFilter
import com.marjorie.scoop.auth.AuthenticationProviderService
import com.marjorie.scoop.auth.filter.CsrfTokenLoggingFilter
import com.marjorie.scoop.auth.filter.RequestValidationFilter
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.web.csrf.CsrfFilter

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

    /**
     * Configures the application to use HTTP Basic as an authentication method. HTTP Basic relies on a username and
     * password for authentication. Also filters requests that do not contain a request ID in the HTTP headers, and if
     * the request was successful logs requests' ID.
     */
    override fun configure(http: HttpSecurity) {
        http.httpBasic()
        http.formLogin().defaultSuccessUrl("/auth", true)

        http.addFilterBefore(
            RequestValidationFilter(),
            BasicAuthenticationFilter::class.java
        ).addFilterAfter(
            CsrfTokenLoggingFilter(),
            CsrfFilter::class.java
        ).addFilterAfter(
            AuthenticationLoggingFilter(),
            BasicAuthenticationFilter::class.java
        ).authorizeRequests()
            .mvcMatchers(HttpMethod.GET, "/").permitAll()
            .and()
         .authorizeRequests()
            .mvcMatchers(HttpMethod.GET, "/auth").authenticated()
            .and()
        .authorizeRequests()
            .mvcMatchers(HttpMethod.GET, "/api/user/*").hasAuthority("ROLE_ADMIN")
            .mvcMatchers(HttpMethod.POST, "/api/user").authenticated()
            .mvcMatchers(HttpMethod.DELETE, "/api/user/*").authenticated()
            .and()
        .authorizeRequests()
            .mvcMatchers(HttpMethod.GET, "/api/venue/*").permitAll()
            .mvcMatchers(HttpMethod.POST, "/api/venue").authenticated()
            .mvcMatchers(HttpMethod.DELETE, "/api/venue/*").authenticated()
            .and()
        .authorizeRequests()
            .mvcMatchers(HttpMethod.GET, "/api/review/*").permitAll()
            .mvcMatchers(HttpMethod.POST, "/api/review").authenticated()
            .mvcMatchers(HttpMethod.DELETE, "/api/review/*").authenticated()

        //http.csrf().disable()
    }
}