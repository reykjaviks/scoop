package com.marjorie.scoop.venue

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

/**
 * Exposes endpoints that serve information on venues.
 */
@RestController
@RequestMapping("/api/venue")
class VenueController(private val venueService: VenueService) {
    @GetMapping("/all")
    fun listVenues(): Iterable<Venue?> = venueService.getVenues()

    @GetMapping("/{id}")
    fun getVenue(@PathVariable id: Long): Venue {
        return venueService.getVenue(id)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                String.format("No venue found for id %s", id)
            )
    }
    
    @GetMapping("/search")
    fun findVenue(@RequestParam query: String): Iterable<Venue?> {
        val venues = venueService.searchVenues(query)

        return if (!venues.isNullOrEmpty())
            venues
        else throw
            ResponseStatusException(
                HttpStatus.NOT_FOUND,
                String.format("No venues found for query %s", query)
            )
    }

}