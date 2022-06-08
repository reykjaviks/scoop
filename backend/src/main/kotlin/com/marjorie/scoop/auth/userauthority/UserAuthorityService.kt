package com.marjorie.scoop.auth.userauthority

import com.marjorie.scoop.auth.authority.Authority
import com.marjorie.scoop.auth.user.User
import org.springframework.stereotype.Service

@Service
class UserAuthorityService(private val userAuthorityRepository: UserAuthorityRepository) {
    fun createUserAuthorityConnection(user: User, authority: Authority): UserAuthority {
        return userAuthorityRepository.save(
                UserAuthority(
                    user = user,
                    authority = authority
                )
            )
    }
}