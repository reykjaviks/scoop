package com.marjorie.scoop.review

/**
 * Contains data that is used to map Review entities.
 */
data class ReviewDTO(
    var review: String,
    var rating: Double,
    var venueId: Long,
    var writer: String
)