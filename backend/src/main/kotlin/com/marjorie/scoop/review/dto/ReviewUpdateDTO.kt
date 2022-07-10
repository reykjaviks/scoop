package com.marjorie.scoop.review.dto

/**
 * Information needed to update an existing review.
 */
data class ReviewUpdateDTO(
    var review: String? = null,
    var rating: Double? = null,
)