package com.marjorie.scoop.auth.authority

import org.springframework.stereotype.Service

@Service
class AuthorityService(private val authorityRepository: AuthorityRepository) {
    fun findByName(authorityName: String): Authority? {
        return authorityRepository.findByName(authorityName)
    }
}