package com.marjorie.scoop.review

import java.time.Instant

data class ReviewDTO(
    var id: Long? = null,
    var review: String? = null,
    var rating: Double? = null,
    var createdAt: Instant? = null,
    var modifiedAt: Instant? = null,
)