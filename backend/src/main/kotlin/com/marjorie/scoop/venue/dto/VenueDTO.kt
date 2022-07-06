package com.marjorie.scoop.venue.dto

import com.marjorie.scoop.neighbourhood.NeighbourhoodDTO
import com.marjorie.scoop.review.dto.ReviewDTONoVenue
import java.time.Instant

data class VenueDTO(
    var id: Long? = null,
    var name: String,
    var description: String? = null,
    var infoUrl: String? = null,
    var imgUrl: String? = null,
    var streetAddress: String,
    var postalCode: String,
    var city: String,
    var neighbourhood: NeighbourhoodDTO? = null,
    var reviewList: List<ReviewDTONoVenue>? = null,
    var createdAt: Instant? = null,
    var modifiedAt: Instant? = null,
)