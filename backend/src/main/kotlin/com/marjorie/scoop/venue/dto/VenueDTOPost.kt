package com.marjorie.scoop.venue.dto

/**
 * Information needed to create a new venue.
 */
data class VenueDTOPost(
    var name: String,
    var description: String? = null,
    var infoUrl: String? = null,
    var imgUrl: String? = null,
    var streetAddress: String,
    var postalCode: String,
    var city: String,
    var neighbourhoodId: Long? = null,
)