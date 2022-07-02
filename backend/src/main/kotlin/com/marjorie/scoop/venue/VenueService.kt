package com.marjorie.scoop.venue

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

/**
 * Handles communication between the Venue repository and Venue controller.
 */
@Service
class VenueService(private val venueRepository: VenueRepository, private val venueMapper: VenueMapper) {
    fun getVenueNew(id: Long): VenueDTO? {
        val venue = venueRepository.findByIdOrNull(id)
        return if (venue == null) {
            null
        } else {
            venueMapper.venueEntityToVenueDTO(venue)
        }
    }

    // todo: refactor to return simple venues
    fun getAllVenues(): List<VenueEntity?> = venueRepository.findAll()

    fun searchVenues(query: String): List<SimpleVenueDTO>? {
        val preparedQuery: String = prepareQueryString(query)
        val venueEntities = venueRepository.findByNameOrAddressOrPostalCodeOrCityOrNeighbourhood(preparedQuery)
        return if (venueEntities.isNullOrEmpty()) {
            null
        } else {
            venueMapper.venueEntitiesToSimpleVenueDTOs(venueEntities)
        }
    }

    private fun prepareQueryString(query: String): String {
        return "%" + query.lowercase() + "%"
    }

    // todo: remove once refactoring is done
    fun getVenue(id: Long): VenueEntity? = venueRepository.findByIdOrNull(id)
}