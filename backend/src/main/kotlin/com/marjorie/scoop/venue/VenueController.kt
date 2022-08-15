package com.marjorie.scoop.venue

import com.marjorie.scoop.common.ScoopResourceAlreadyExistsException
import com.marjorie.scoop.common.ScoopResourceNotFoundException
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
    fun getVenue(@PathVariable id: Long): VenueGetDTO {
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
    fun createVenue(@RequestBody venuePostDTO: VenuePostDTO): VenueGetDTO? {
        try {
            return venueService.createVenue(venuePostDTO)
        } catch (e: ScoopResourceAlreadyExistsException) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "Venue '${venuePostDTO.name}' already exists")
        }
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateVenue(@PathVariable id: Long, @RequestBody venueUpdateDTO: VenueUpdateDTO): VenueGetDTO {
        try {
            return venueService.updateVenue(id, venueUpdateDTO)
        } catch (e: ScoopResourceNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Error in updating a review: ${e.message}")
        }
    }
}