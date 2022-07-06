package com.marjorie.scoop.review.dto

import com.marjorie.scoop.auth.user.dto.UserDTOIdUsername
import java.time.Instant

/**
 * Does not contain information regarding the venue.
 */
data class ReviewDTONoVenue(
    var id: Long? = null,
    var review: String,
    var rating: Double,
    var user: UserDTOIdUsername,
    var createdAt: Instant? = null,
    var modifiedAt: Instant? = null,
)