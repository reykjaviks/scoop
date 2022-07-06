package com.marjorie.scoop.auth.user.dto

/**
 * The most simplified version of the user entity. Transfers only the id and username.
 */
data class UserDTOIdUsername(
    var id: Long,
    var username: String,
)