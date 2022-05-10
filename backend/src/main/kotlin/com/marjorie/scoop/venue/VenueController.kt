package com.marjorie.scoop.venue

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*

/**
 * Exposes endpoints that serve information on venues.
 * */
@RestController
@RequestMapping("/api/venue")
class VenueController(private val venueService: VenueService) {
    @GetMapping("/all")
    fun listVenues(): Iterable<Venue?> = venueService.getVenues()

    @GetMapping("/{id}")
    fun getVenue(@PathVariable id: Long): Venue {
        val venue: Optional<Venue?> = venueService.getVenue(id)
        if (venue.isEmpty) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No resource found for id %s", id))
        }
        return venue.get()
    }

}