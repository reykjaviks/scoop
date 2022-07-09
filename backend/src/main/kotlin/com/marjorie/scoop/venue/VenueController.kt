package com.marjorie.scoop.venue

import com.marjorie.scoop.venue.dto.*
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
        return venueService.getVenueDTO(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "No venue found for id $id")
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    fun getAllVenues(): Iterable<VenueSearchDTO> {
        return venueService.getAllVenues()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "The result of getting all venues was empty")
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    fun searchVenues(@RequestParam query: String): Iterable<VenueSearchDTO> {
        return venueService.searchVenues(query)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "No venues found for query $query")
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    fun createVenue(@RequestBody venuePostDTO: VenuePostDTO): VenueDTO? {
        if (venueService.venueExists(venuePostDTO.name)) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "Venue '${venuePostDTO.name}' already exists")
        }
        return venueService.createVenue(venuePostDTO)
    }

    // todo: fix exception handling
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateVenue(@PathVariable id: Long, @RequestBody venueUpdateDTO: VenueUpdateDTO): VenueDTO {
        return venueService.updateVenue(id, venueUpdateDTO)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Could not update the review because review $id does not exist")
    }
}