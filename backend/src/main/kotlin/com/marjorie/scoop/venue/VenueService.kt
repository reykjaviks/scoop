package com.marjorie.scoop.venue

import org.springframework.stereotype.Service
import java.util.*

/**
 * Handles the communication between Venue repository and Venue controller.
 * */
@Service
class VenueService(private val venueRepository : VenueRepository) {
    fun getVenues(): List<Venue?> = venueRepository.findAll()
    fun getVenue(id: Long): Optional<Venue?> = venueRepository.findById(id)
}