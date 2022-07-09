package com.marjorie.scoop.venue.dto

import com.marjorie.scoop.neighbourhood.dto.NeighbourhoodDTO
import java.time.Instant

/**
 * Results displayed by the venue search. Does not transfer information regarding venue's reviews.
 */
data class VenueSearchDTO(
    var id: Long,
    var name: String,
    var description: String? = null,
    var infoUrl: String? = null,
    var imgUrl: String? = null,
    var streetAddress: String,
    var postalCode: String,
    var city: String,
    var neighbourhood: NeighbourhoodDTO? = null,
    var createdAt: Instant,
    var modifiedAt: Instant? = null,
)