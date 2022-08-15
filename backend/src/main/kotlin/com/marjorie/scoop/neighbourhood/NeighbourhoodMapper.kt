package com.marjorie.scoop.neighbourhood

import com.marjorie.scoop.neighbourhood.dto.NeighbourhoodGetDTO
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
interface NeighbourhoodMapper{
    fun mapToNeighbourhoodEntity(neighbourhoodGetDTO: NeighbourhoodGetDTO): NeighbourhoodEntity
    fun mapToNeighbourhoodGetDTO(neighbourhoodEntity: NeighbourhoodEntity): NeighbourhoodGetDTO
    fun mapToNeighbourhoodDTOs(neighbourhoodEntities: List<NeighbourhoodEntity>): List<NeighbourhoodGetDTO>
}