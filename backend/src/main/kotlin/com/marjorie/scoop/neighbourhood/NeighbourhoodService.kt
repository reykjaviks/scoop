package com.marjorie.scoop.neighbourhood

import com.marjorie.scoop.neighbourhood.dto.NeighbourhoodDTO
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

/**
 * Handles communication between the Neighbourhood repository and Neighbourhood controller.
 */
@Service
class NeighbourhoodService(
    private val neighbourhoodRepository: NeighbourhoodRepository,
    private val neighbourhoodMapper: NeighbourhoodMapper,
) {
    fun getNeighbourhood(id: Long): NeighbourhoodDTO? {
        val neighbourhood = neighbourhoodRepository.findByIdOrNull(id)
        if (neighbourhood != null) {
            return neighbourhoodMapper.mapToNeighbourhoodDTO(neighbourhood)
        }
        return null
    }

    fun getNeighbourhoodEntity(id: Long): NeighbourhoodEntity? {
        return neighbourhoodRepository.findByIdOrNull(id)
    }

    fun getAllNeighbourhoods(): List<NeighbourhoodDTO>? {
        val neighbourhoods = neighbourhoodRepository.findAll() as List<NeighbourhoodEntity>
        if (neighbourhoods.isNotEmpty()) {
            return neighbourhoodMapper.mapToNeighbourhoodDTOs(neighbourhoods)
        }
        return null
    }
}