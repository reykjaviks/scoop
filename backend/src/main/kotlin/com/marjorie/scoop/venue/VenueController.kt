package com.marjorie.scoop.venue

import org.springframework.web.bind.annotation.*
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
    fun getVenue(@PathVariable id: Long): Optional<Venue?> = venueService.getVenue(id)
}