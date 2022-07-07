package com.marjorie.scoop.neighbourhood

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

/**
 * Handles communication between the Neighbourhood repository and Neighbourhood controller.
 */
@Service
class NeighbourhoodService(private val neighbourhoodRepository: NeighbourhoodRepository) {
    fun getAllNeighbourhoods(): List<NeighbourhoodEntity?> = neighbourhoodRepository.findAll()
    fun getNeighbourhood(id: Long): NeighbourhoodEntity? = neighbourhoodRepository.findByIdOrNull(id)
}