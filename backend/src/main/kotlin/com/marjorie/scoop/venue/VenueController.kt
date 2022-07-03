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
    fun getVenue(@PathVariable id: Long): VenueDTO {
        return venueService.getVenueNew(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "No venue found for id $id")
    }

    @GetMapping("/all")
    fun getAllVenues(): Iterable<SimpleVenueDTO> {
        return venueService.getAllVenues()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "The result of getting all venues was empty")
    }

    @GetMapping("/search")
    fun searchVenues(@RequestParam query: String): Iterable<SimpleVenueDTO> {
        return venueService.searchVenues(query)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "No venues found for query $query")
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    fun createVenue(@RequestBody simpleVenueDTO: SimpleVenueDTO) {
        venueService.createVenue(simpleVenueDTO)
            ?: throw ResponseStatusException(HttpStatus.CONFLICT, "Venue '${simpleVenueDTO.name}' already exists")
    }

    @PatchMapping("/{id}")
    fun updateVenue(@PathVariable id: Long, @RequestBody simpleVenueDTO: SimpleVenueDTO) {
        // todo: implement later
    }
}