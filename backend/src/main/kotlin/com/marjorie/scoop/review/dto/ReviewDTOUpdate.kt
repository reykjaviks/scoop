package com.marjorie.scoop.review.dto

/**
 * Information needed to update an existing review.
 */
data class ReviewDTOUpdate(
    var review: String? = null,
    var rating: Double? = null,
)