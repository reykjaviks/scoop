package com.marjorie.scoop.venue

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

/**
 * Handles the communication between Venue repository and Venue controller.
 * */
@Service
class VenueService(private val venueRepository : VenueRepository) {
    fun getVenues(): List<Venue?> = venueRepository.findAll()
    fun getVenue(id: Long): Venue? = venueRepository.findByIdOrNull(id)
}