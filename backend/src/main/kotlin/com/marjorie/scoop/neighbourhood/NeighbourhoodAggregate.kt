package com.marjorie.scoop.neighbourhood

/**
 * Used to project JPA query results into a custom object. This object contains info on how many
 * venues reside in a particular neighbourhood and stores an img link of a random venue residing in that neighbourhood.
 */
interface NeighbourhoodAggregate {
    fun getNeighbourhoodName(): String
    fun getImgUrl(): String?
    fun getVenueCount(): Long
}