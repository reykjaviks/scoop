package com.marjorie.scoop.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
class WebAuthorizationConfig: WebSecurityConfigurerAdapter() {
}