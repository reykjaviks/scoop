package com.marjorie.scoop.venue

import com.marjorie.scoop.common.ScoopResourceAlreadyExistsException
import com.marjorie.scoop.common.ScoopResourceNotFoundException
import com.marjorie.scoop.venue.dto.*
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

    fun getAllVenues(): List<VenueSearchDTO>? {
        val allVenues = venueRepository.findAll() as List<VenueEntity>
        return if (allVenues.isEmpty()) {
            null
        } else {
            venueMapper.mapToVenueSearchDTOs(allVenues)
        }
    }

    fun searchVenues(query: String): List<VenueSearchDTO>? {
        val preparedQuery: String = prepareQueryString(query)
        val venueEntities = venueRepository.findByNameOrAddressOrPostalCodeOrCityOrNeighbourhood(preparedQuery)
        return if (venueEntities.isNullOrEmpty()) {
            null
        } else {
            venueMapper.mapToVenueSearchDTOs(venueEntities)
        }
    }

    fun createVenue(venuePostDTO: VenuePostDTO): VenueDTO {
        if (this.venueExists(venuePostDTO.name)) {
            throw ScoopResourceAlreadyExistsException("Venue '${venuePostDTO.name}' already exists")
        } else {
            val savedVenue = venueRepository.save(venueMapper.mapToVenueEntity(venuePostDTO))
            return venueMapper.mapToVenueDTO(savedVenue)
        }
    }

    fun updateVenue(id: Long, venueUpdateDTO: VenueUpdateDTO): VenueDTO {
        val venue = this.getVenueEntity(id)
        if (venue == null) {
            throw ScoopResourceNotFoundException("No venue found for ID $id")
        } else {
            val updatedVenueEntity = venueMapper.updateVenueEntity(venueUpdateDTO, venue)
            return venueMapper.mapToVenueDTO(updatedVenueEntity)
        }
    }

    fun venueExists(name: String): Boolean {
        return venueRepository.existsByName(name)
    }

    private fun prepareQueryString(query: String): String {
        return "%" + query.lowercase() + "%"
    }
}