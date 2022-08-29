package com.marjorie.scoop.auth.models.userauthority

import com.marjorie.scoop.auth.models.authority.Authority
import com.marjorie.scoop.auth.models.user.UserEntity
import org.springframework.stereotype.Service

@Service
class UserAuthorityService(private val userAuthorityRepository: UserAuthorityRepository) {
    fun createUserAuthorityConnection(user: UserEntity, authority: Authority): UserAuthority {
        return userAuthorityRepository.save(
                UserAuthority(
                    user = user,
                    authority = authority
                )
            )
    }
}