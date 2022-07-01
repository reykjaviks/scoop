package com.marjorie.scoop.review

/**
 * Contains data that is used to create Review entities.
 */
data class ReviewData(
    val review: String? = null,
    val rating: Double? = null,
    val venueId: Long? = null,
    val writer: String? = null,
)