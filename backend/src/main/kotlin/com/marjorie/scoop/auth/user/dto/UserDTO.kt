package com.marjorie.scoop.auth.user.dto

import com.marjorie.scoop.auth.authority.Authority
import com.marjorie.scoop.review.ReviewEntity

data class UserDTO(
    var name: String,
    var username: String,
    var authorities: MutableList<Authority>,
    var reviewList: MutableList<ReviewEntity>? = null,
)