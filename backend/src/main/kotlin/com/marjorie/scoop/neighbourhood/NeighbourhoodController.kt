package com.marjorie.scoop.neighbourhood

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

/**
 * Exposes endpoints that serve information on neighbourhoods.
 */
@RestController
@RequestMapping("/api/neighbourhood")
class NeighbourhoodController(private val neighbourhoodService: NeighbourhoodService) {
    @GetMapping("/{id}")
    fun getNeighbourhood(@PathVariable id: Long): NeighbourhoodEntity {
        return neighbourhoodService.getNeighbourhood(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "No neighbourhood found for id $id")
    }

    @GetMapping("/all")
    fun getAllNeighbourhoods(): Iterable<NeighbourhoodEntity?> = neighbourhoodService.getAllNeighbourhoods()
}