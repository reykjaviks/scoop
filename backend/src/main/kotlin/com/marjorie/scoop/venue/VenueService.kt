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
    fun getVenueDTO(id: Long): VenueGetDTO? {
        val venueEntity = venueRepository.findByIdOrNull(id) ?: return null
        return venueMapper.mapToVenueGetDTO(venueEntity)
    }

    fun getVenueEntity(id: Long): VenueEntity? {
        return venueRepository.findByIdOrNull(id)
    }

    fun getAllVenues(): List<VenueSearchDTO>? {
        val allVenues = venueRepository.findAll() as List<VenueEntity>
        if (allVenues.isEmpty()) return null
        return venueMapper.mapToVenueSearchDTOs(allVenues)
    }

    fun searchVenues(query: String): List<VenueSearchDTO>? {
        val preparedQuery: String = prepareQueryString(query)
        val venueEntities = venueRepository.findByNameOrAddressOrPostalCodeOrCityOrNeighbourhood(preparedQuery)
        if (venueEntities.isNullOrEmpty()) return null
        return venueMapper.mapToVenueSearchDTOs(venueEntities)
    }

    fun createVenue(venuePostDTO: VenuePostDTO): VenueGetDTO {
        if (this.venueExists(venuePostDTO.name)) {
            throw ScoopResourceAlreadyExistsException("Venue '${venuePostDTO.name}' already exists")
        }
        val savedVenue = venueRepository.save(venueMapper.mapToVenueEntity(venuePostDTO))
        return venueMapper.mapToVenueGetDTO(savedVenue)
    }

    fun updateVenue(id: Long, venueUpdateDTO: VenueUpdateDTO): VenueGetDTO {
        val venue = this.getVenueEntity(id) ?: throw ScoopResourceNotFoundException("No venue found for ID $id")
        val updatedVenueEntity = venueMapper.updateVenueEntity(venueUpdateDTO, venue)
        return venueMapper.mapToVenueGetDTO(updatedVenueEntity)
    }

    fun venueExists(name: String): Boolean {
        return venueRepository.existsByName(name)
    }

    fun venueExists(id: Long): Boolean {
        return venueRepository.existsById(id)
    }

    private fun prepareQueryString(query: String): String {
        return "%" + query.lowercase() + "%"
    }
}