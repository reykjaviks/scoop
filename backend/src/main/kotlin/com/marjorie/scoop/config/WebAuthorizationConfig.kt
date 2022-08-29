package com.marjorie.scoop.config

import com.marjorie.scoop.auth.UsernamePasswordAuthProvider
import com.marjorie.scoop.auth.filters.InitialAuthFilter
import com.marjorie.scoop.auth.filters.JwtAuthFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import java.util.*

/**
 * Manages configuration info regarding web authorization.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebAuthorizationConfig(
    private val usernamePasswordAuthProvider: UsernamePasswordAuthProvider,
    private val jwtAuthFilter: JwtAuthFilter,
    private val initialAuthFilter: InitialAuthFilter
): WebSecurityConfigurerAdapter() {
    /**
     * Registers a custom authentication provider to Spring Security's authentication manager.
     */
    override fun configure(authManagerBuilder: AuthenticationManagerBuilder) {
         authManagerBuilder.authenticationProvider(usernamePasswordAuthProvider)
    }

    /** General configuration info (login type, CSRF, CORS...) */
    override fun configure(httpSecurity: HttpSecurity) {
        /** CSRF is not needed when using JWT tokens */
        httpSecurity.csrf().disable()

        /** Filter chain */
        httpSecurity.addFilterAt(
            initialAuthFilter,
            BasicAuthenticationFilter::class.java
        ).addFilterAfter(
            jwtAuthFilter,
            BasicAuthenticationFilter::class.java
        )

        /* Disabled while testing
        httpSecurity.addFilterBefore(
            RequestValidationFilter(),
            BasicAuthenticationFilter::class.java
        ).addFilterAfter(
            RequestLoggingFilter(),
            BasicAuthenticationFilter::class.java
        )
        */

        /**
         * Access rules for various endpoints. Please note that the application uses a combination of
         * web security and method security.
         */
        httpSecurity.authorizeRequests()
            .mvcMatchers(HttpMethod.GET, "/").permitAll()
            .mvcMatchers(HttpMethod.POST, "/").denyAll()
            .mvcMatchers(HttpMethod.DELETE, "/").denyAll()

            .mvcMatchers(HttpMethod.GET, "/auth").authenticated()

            .mvcMatchers(HttpMethod.GET, "/api/user/*").authenticated()
            .mvcMatchers(HttpMethod.POST, "/api/user/add").permitAll()
            .mvcMatchers(HttpMethod.PATCH, "/api/user/*").authenticated()
            .mvcMatchers(HttpMethod.DELETE, "/api/user/*").authenticated()

            .mvcMatchers(HttpMethod.GET, "/api/venue/*").permitAll()
            .mvcMatchers(HttpMethod.POST, "/api/venue/add").authenticated()
            .mvcMatchers(HttpMethod.PATCH, "/api/venue/*").authenticated()
            .mvcMatchers(HttpMethod.DELETE, "/api/venue/*").authenticated()

            .mvcMatchers(HttpMethod.GET, "/api/review/*").permitAll()
            .mvcMatchers(HttpMethod.POST, "/api/review/add").authenticated()
            .mvcMatchers(HttpMethod.PATCH, "/api/review/*").authenticated()
            .mvcMatchers(HttpMethod.DELETE, "/api/review/*").authenticated()
    }

    /**
     * Adds the AuthenticationManager to the Spring context so that we can autowire
     * it from the InitialAuthFilter class.
     */
    @Bean
    override fun authenticationManager(): AuthenticationManager {
        return super.authenticationManager()
    }

    @Bean
    fun corsFilter(): CorsFilter? {
        val corsConfiguration = CorsConfiguration()
        corsConfiguration.allowCredentials = true
        corsConfiguration.allowedOrigins = Arrays.asList("http://localhost:4200")
        corsConfiguration.allowedHeaders = Arrays.asList(
            "Origin", "Access-Control-Allow-Origin", "Content-Type",
            "Accept", "Authorization", "Origin, Accept", "X-Requested-With",
            "Access-Control-Request-Method", "Access-Control-Request-Headers"
        )
        corsConfiguration.exposedHeaders = Arrays.asList(
            "Origin", "Content-Type", "Accept", "Authorization",
            "Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"
        )
        corsConfiguration.allowedMethods = Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")
        val urlBasedCorsConfigurationSource = UrlBasedCorsConfigurationSource()
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration)
        return CorsFilter(urlBasedCorsConfigurationSource)
    }
}