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
    @GetMapping("/{id}")
    fun getVenue(@PathVariable id: Long): VenueEntity? {
        return venueService.getVenue(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "No venue found for id $id")
    }

    @GetMapping("/all")
    fun getAllVenues(): Iterable<VenueEntity?> = venueService.getAllVenues()

    @GetMapping("/search")
    fun searchVenues(@RequestParam query: String): Iterable<VenueEntity?> {
        val venues = venueService.searchVenues(query)
        return if (!venues.isNullOrEmpty())
            venues
        else throw
            ResponseStatusException(HttpStatus.NOT_FOUND, "No venues found for query $query")
    }
}