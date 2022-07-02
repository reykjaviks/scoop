package com.marjorie.scoop.venue

import com.marjorie.scoop.neighbourhood.NeighbourhoodDTO

/**
 * Simplified version of the venue entity. Does not return venue's reviews.
 */
data class SimpleVenueDTO (
    var name: String,
    var description: String? = null,
    var streetAddress: String,
    var postalCode: String,
    var city: String,
    var neighbourhoodDTO: NeighbourhoodDTO? = null,
)