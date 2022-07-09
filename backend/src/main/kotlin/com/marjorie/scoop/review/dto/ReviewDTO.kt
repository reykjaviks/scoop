package com.marjorie.scoop.review.dto

import com.marjorie.scoop.auth.user.dto.UserDTOIdUsername
import com.marjorie.scoop.venue.dto.VenueSlimDTO
import java.time.Instant

data class ReviewDTO(
    var id: Long,
    var review: String,
    var rating: Double,
    var venue: VenueSlimDTO? = null,
    var user: UserDTOIdUsername? = null,
    var createdAt: Instant,
    var modifiedAt: Instant? = null,
)