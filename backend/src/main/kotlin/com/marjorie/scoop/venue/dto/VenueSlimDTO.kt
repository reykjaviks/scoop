package com.marjorie.scoop.venue.dto

/**
 * Transfers only venue's id and name information.
 */
data class VenueSlimDTO(
    var id: Long,
    var name: String,
)