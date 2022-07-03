package com.marjorie.scoop.venue

import com.marjorie.scoop.neighbourhood.NeighbourhoodDTO
import com.marjorie.scoop.review.ReviewDTO

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
    var reviewList: List<ReviewDTO>? = null,
)