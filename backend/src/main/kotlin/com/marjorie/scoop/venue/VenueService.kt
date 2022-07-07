package com.marjorie.scoop.venue

import com.marjorie.scoop.common.ScoopException
import com.marjorie.scoop.neighbourhood.NeighbourhoodService
import com.marjorie.scoop.venue.dto.VenueDTONoReviews
import com.marjorie.scoop.venue.dto.VenueDTO
import com.marjorie.scoop.venue.dto.VenueDTOPost
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

/**
 * Handles communication between the venue repository and venue controller.
 */
@Service
class VenueService(
    private val venueRepository: VenueRepository,
    private val venueMapper: VenueMapper,
) {
    fun getVenueDTO(id: Long): VenueDTO? {
        val venueEntity = venueRepository.findByIdOrNull(id)
        return if (venueEntity == null) {
            null
        } else {
            venueMapper.mapToVenueDTO(venueEntity)
        }
    }

    fun getVenueEntity(id: Long): VenueEntity? {
        return venueRepository.findByIdOrNull(id)
    }

    fun getAllVenues(): List<VenueDTONoReviews>? {
        val allVenues = venueRepository.findAll() as List<VenueEntity>
        return if (allVenues.isEmpty()) {
            null
        } else {
            venueMapper.mapToVenueDTONoReviewsList(allVenues)
        }
    }

    fun searchVenues(query: String): List<VenueDTONoReviews>? {
        val preparedQuery: String = prepareQueryString(query)
        val venueEntities = venueRepository.findByNameOrAddressOrPostalCodeOrCityOrNeighbourhood(preparedQuery)
        return if (venueEntities.isNullOrEmpty()) {
            null
        } else {
            venueMapper.mapToVenueDTONoReviewsList(venueEntities)
        }
    }

    fun createVenue(venueDTOPost: VenueDTOPost): VenueDTO? {
        if (this.venueExists(venueDTOPost.name)) {
            throw ScoopException("Venue '${venueDTOPost.name}' already exists")
        } else {
            val savedVenue = venueRepository.save(venueMapper.mapToVenueEntity(venueDTOPost))
            return venueMapper.mapToVenueDTO(savedVenue)
        }
    }

    fun updateVenue(id: Long, venueDTONoReviews: VenueDTONoReviews): VenueDTO? {
        val venue = venueRepository.findByIdOrNull(id)
        return if (venue == null) {
            null
        } else {
            venueMapper.mapToVenueDTO(
                venueMapper.updateVenueEntity(venueDTONoReviews, venue)
            )
        }
    }

    fun venueExists(name: String): Boolean {
        return venueRepository.existsByName(name)
    }

    private fun prepareQueryString(query: String): String {
        return "%" + query.lowercase() + "%"
    }

}