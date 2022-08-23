package com.marjorie.scoop.neighbourhood

import com.marjorie.scoop.neighbourhood.dto.NeighbourhoodGetDTO
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

/**
 * Exposes endpoints that serve information on neighbourhoods.
 */
@RestController
@RequestMapping("/api/neighbourhood")
class NeighbourhoodController(private val neighbourhoodService: NeighbourhoodService) {
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getNeighbourhood(@PathVariable id: Long): NeighbourhoodGetDTO {
        return neighbourhoodService.getNeighbourhood(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "No neighbourhood found for id $id")
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    fun getAllNeighbourhoods(): Iterable<NeighbourhoodGetDTO> {
        return neighbourhoodService.getAllNeighbourhoods()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "The result of getting all neighbourhoods was empty")
    }

    @GetMapping("/venue-count")
    @ResponseStatus(HttpStatus.OK)
    fun getVenueCountByNeighbourhood(): Iterable<NeighbourhoodAggregate> {
        return neighbourhoodService.getVenueCountByNeighbourhood()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "The result of counting venues by neighbourhood was empty")
    }
}