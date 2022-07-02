package com.marjorie.scoop.venue

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

/**
 * Handles communication between the Venue repository and Venue controller.
 */
@Service
class VenueService(private val venueRepository: VenueRepository) {
    fun getVenue(id: Long): VenueEntity? = venueRepository.findByIdOrNull(id)

    fun getAllVenues(): List<VenueEntity?> = venueRepository.findAll()

    fun searchVenues(query: String): List<VenueEntity>? {
        val preparedQuery: String = prepareQueryString(query)
        return venueRepository.findByNameOrAddressOrPostalCodeOrCityOrNeighbourhood(preparedQuery)
    }

    private fun prepareQueryString(query: String): String {
        return "%" + query.lowercase() + "%"
    }
}