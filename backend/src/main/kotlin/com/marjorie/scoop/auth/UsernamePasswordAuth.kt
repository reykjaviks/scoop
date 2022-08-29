package com.marjorie.scoop.auth

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class UsernamePasswordAuth: UsernamePasswordAuthenticationToken {
    /**
     * Sets the authentication object as authenticated. Used by the authentication provider to authenticate
     * a request.
     */
    constructor(principal: Any, credentials: Any?, authorities: Collection<GrantedAuthority>) : super(
        principal,
        credentials,
        authorities
    ) { }

    /**
     * Authentication instance remains unauthenticated. Used for building the authentication object.
     */
    constructor(principal: Any, credentials: Any) : super(principal, credentials) {}
}