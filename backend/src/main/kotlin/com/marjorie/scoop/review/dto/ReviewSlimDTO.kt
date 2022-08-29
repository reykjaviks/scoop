package com.marjorie.scoop.review.dto

import com.marjorie.scoop.auth.models.user.dto.UserSlimDTO
import java.time.Instant

/**
 * Does not contain information regarding the venue.
 */
data class ReviewSlimDTO(
    var id: Long,
    var review: String,
    var rating: Double,
    var user: UserSlimDTO,
    var createdAt: Instant,
    var modifiedAt: Instant? = null,
)