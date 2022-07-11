package com.marjorie.scoop.neighbourhood

import com.marjorie.scoop.neighbourhood.dto.NeighbourhoodDTO
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
interface NeighbourhoodMapper{
    fun mapToNeighbourhoodEntity(neighbourhoodDTO: NeighbourhoodDTO): NeighbourhoodEntity
    fun mapToNeighbourhoodDTO(neighbourhoodEntity: NeighbourhoodEntity): NeighbourhoodDTO
    fun mapToNeighbourhoodDTOs(neighbourhoodEntities: List<NeighbourhoodEntity>): List<NeighbourhoodDTO>
}