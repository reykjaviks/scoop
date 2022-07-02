package com.marjorie.scoop.venue

import com.marjorie.scoop.neighbourhood.NeighbourhoodDTO

/**
 * Simplified version of the venue entity. Does not transfer venue's reviews.
 */
data class SimpleVenueDTO (
    var id: Long? = null,
    var name: String,
    var description: String? = null,
    var streetAddress: String,
    var postalCode: String,
    var city: String,
    var neighbourhood: NeighbourhoodDTO? = null,
)