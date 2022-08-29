package com.marjorie.scoop.auth.models.user.dto

/**
 * The most simplified version of the user entity. Transfers only the id, name and username.
 */
data class UserSlimDTO(
    var id: Long,
    var name: String,
    var username: String,
)