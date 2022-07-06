package com.marjorie.scoop.review.dto

/**
 * Information needed to create a new review.
 */
data class ReviewDTOPost(
    var review: String,
    var rating: Double,
    var venueId: Long,
    var username: String,
)