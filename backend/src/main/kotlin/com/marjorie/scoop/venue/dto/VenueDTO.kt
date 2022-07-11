package com.marjorie.scoop.venue.dto

import com.marjorie.scoop.neighbourhood.dto.NeighbourhoodDTO
import com.marjorie.scoop.review.dto.ReviewSlimDTO
import java.time.Instant

data class VenueDTO(
    var id: Long,
    var name: String,
    var description: String? = null,
    var infoUrl: String? = null,
    var imgUrl: String? = null,
    var streetAddress: String,
    var postalCode: String,
    var city: String,
    var neighbourhood: NeighbourhoodDTO? = null,
    var reviewList: List<ReviewSlimDTO>? = null,
    var createdAt: Instant,
    var modifiedAt: Instant? = null,
)