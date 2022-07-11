package com.marjorie.scoop.review.dto

import com.marjorie.scoop.auth.user.dto.UserSlimDTO
import com.marjorie.scoop.venue.dto.VenueSlimDTO
import java.time.Instant

data class ReviewDTO(
    var id: Long,
    var review: String,
    var rating: Double,
    var venue: VenueSlimDTO? = null,
    var user: UserSlimDTO? = null,
    var createdAt: Instant,
    var modifiedAt: Instant? = null,
)