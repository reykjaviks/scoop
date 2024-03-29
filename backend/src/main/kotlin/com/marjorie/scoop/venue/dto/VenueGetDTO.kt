package com.marjorie.scoop.venue.dto

import com.marjorie.scoop.neighbourhood.dto.NeighbourhoodGetDTO
import com.marjorie.scoop.review.dto.ReviewSlimDTO
import java.time.Instant

data class VenueGetDTO(
    var id: Long,
    var name: String,
    var description: String? = null,
    var infoUrl: String? = null,
    var imgUrl: String? = null,
    var streetAddress: String,
    var postalCode: String,
    var city: String,
    var neighbourhood: NeighbourhoodGetDTO? = null,
    var reviewList: List<ReviewSlimDTO>? = null,
    var createdAt: Instant,
    var modifiedAt: Instant? = null,
)