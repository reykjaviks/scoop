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
    @ResponseStatus(HttpStatus.OK)
    fun getVenue(@PathVariable id: Long): VenueDTO {
        return venueService.getVenueNew(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "No venue found for id $id")
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    fun getAllVenues(): Iterable<SimpleVenueDTO> {
        return venueService.getAllVenues()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "The result of getting all venues was empty")
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    fun searchVenues(@RequestParam query: String): Iterable<SimpleVenueDTO> {
        return venueService.searchVenues(query)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "No venues found for query $query")
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    fun createVenue(@RequestBody simpleVenueDTO: SimpleVenueDTO): SimpleVenueDTO {
        return venueService.createVenue(simpleVenueDTO)
            ?: throw ResponseStatusException(HttpStatus.CONFLICT, "Venue '${simpleVenueDTO.name}' already exists")
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateVenue(@PathVariable id: Long, @RequestBody simpleVenueDTO: SimpleVenueDTO): SimpleVenueDTO {
        return venueService.updateVenue(id, simpleVenueDTO)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Could not update the review because review $id does not exist")
    }
}