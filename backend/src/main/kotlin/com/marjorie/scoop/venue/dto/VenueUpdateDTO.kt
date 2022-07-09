package com.marjorie.scoop.venue.dto

/**
 * Information needed to update an existing venue.
 */
data class VenueUpdateDTO(
    var name: String? = null,
    var description: String? = null,
    var infoUrl: String? = null,
    var imgUrl: String? = null,
    var streetAddress: String? = null,
    var postalCode: String? = null,
    var city: String? = null,
    var neighbourhoodId: Long? = null,
)