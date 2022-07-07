package com.marjorie.scoop.venue

import com.marjorie.scoop.venue.dto.VenueDTONoReviews
import com.marjorie.scoop.venue.dto.VenueDTO
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

/**
 * Handles communication between the venue repository and venue controller.
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

    fun getAllVenues(): List<VenueDTONoReviews>? {
        val allVenues = venueRepository.findAll() as List<VenueEntity>
        return if (allVenues.isEmpty()) {
            null
        } else {
            venueMapper.venueEntitiesToVenueDTONoReviews(allVenues)
        }
    }

    fun searchVenues(query: String): List<VenueDTONoReviews>? {
        val preparedQuery: String = prepareQueryString(query)
        val venueEntities = venueRepository.findByNameOrAddressOrPostalCodeOrCityOrNeighbourhood(preparedQuery)
        return if (venueEntities.isNullOrEmpty()) {
            null
        } else {
            venueMapper.venueEntitiesToVenueDTONoReviews(venueEntities)
        }
    }

    // todo: return entity
    fun createVenue(venueDTONoReviews: VenueDTONoReviews): VenueDTO? {
        val venueExists = venueRepository.existsByName(venueDTONoReviews.name)
        return if (venueExists) {
            null
        } else {
            venueMapper.venueEntityToVenueDTO(
                venueRepository.save(venueMapper.venueDTONoReviewsToVenueEntity(venueDTONoReviews))
            )
        }
    }

    fun updateVenue(id: Long, venueDTONoReviews: VenueDTONoReviews): VenueDTO? {
        val venue = venueRepository.findByIdOrNull(id)
        return if (venue == null) {
            null
        } else {
            venueMapper.venueEntityToVenueDTO(
                venueMapper.updateVenueEntityFromVenueDTONoReviews(venueDTONoReviews, venue)
            )
        }
    }

    private fun prepareQueryString(query: String): String {
        return "%" + query.lowercase() + "%"
    }

    // todo: remove once refactoring is done
    fun getVenue(id: Long): VenueEntity? = venueRepository.findByIdOrNull(id)
}