package com.marjorie.scoop.venue

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

/**
 * Handles communication between the Venue repository and Venue controller.
 */
@Service
class VenueService(private val venueRepository: VenueRepository, private val venueMapper: VenueMapper) {
    // todo: remove once refactoring is done
    fun getVenue(id: Long): VenueEntity? = venueRepository.findByIdOrNull(id)

    fun getVenueNew(id: Long): VenueDTO? {
        val venue = venueRepository.findByIdOrNull(id)
        return if (venue == null)
            null
        else
            venueMapper.venueEntityToVenueDTO(venue)
    }

    // todo: refactor to return venuedtos
    fun getAllVenues(): List<VenueEntity?> = venueRepository.findAll()

    // todo: refactor to return venuedtos
    fun searchVenues(query: String): List<VenueEntity>? {
        val preparedQuery: String = prepareQueryString(query)
        return venueRepository.findByNameOrAddressOrPostalCodeOrCityOrNeighbourhood(preparedQuery)
    }

    private fun prepareQueryString(query: String): String {
        return "%" + query.lowercase() + "%"
    }
}