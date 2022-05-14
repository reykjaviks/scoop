package com.marjorie.scoop.venue

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

/**
 * Handles the communication between the Venue repository and Venue controller.
 * */
@Service
class VenueService(private val venueRepository : VenueRepository) {
    fun getVenues(): List<Venue?> = venueRepository.findAll()

    fun getVenue(id: Long): Venue? = venueRepository.findByIdOrNull(id)

    fun searchVenues(query: String): List<Venue>? {
        val preparedQuery: String = prepareQueryString(query)
        return venueRepository.findByNameOrAddressOrPostalCodeOrCityOrNeighbourhood(preparedQuery)
    }

    private fun prepareQueryString(query: String): String {
        return "%" + query.lowercase() + "%"
    }
}