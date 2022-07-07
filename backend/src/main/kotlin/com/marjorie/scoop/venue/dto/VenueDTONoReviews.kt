package com.marjorie.scoop.venue.dto

import com.marjorie.scoop.neighbourhood.dto.NeighbourhoodDTO
import java.time.Instant

/**
 * Simplified version of the venue entity. Does not transfer information regarding venue's reviews.
 */
data class VenueDTONoReviews (
    var id: Long,
    var name: String,
    var description: String? = null,
    var streetAddress: String,
    var postalCode: String,
    var city: String,
    var neighbourhood: NeighbourhoodDTO? = null,
    var createdAt: Instant,
    var modifiedAt: Instant? = null,
)