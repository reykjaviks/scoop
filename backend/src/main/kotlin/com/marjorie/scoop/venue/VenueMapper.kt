package com.marjorie.scoop.venue

import com.marjorie.scoop.neighbourhood.NeighbourhoodService
import com.marjorie.scoop.venue.dto.*
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy

@Mapper(
    componentModel = "spring",
    uses = [NeighbourhoodService::class],
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
interface VenueMapper {
    fun mapToVenueEntity(venueGetDTO: VenueGetDTO): VenueEntity
    @Mapping(source = "neighbourhoodId", target = "neighbourhood")
    fun mapToVenueEntity(venuePostDTO: VenuePostDTO): VenueEntity
    fun mapToVenueEntity(venueSlimDTO: VenueSlimDTO): VenueEntity
    fun mapToVenueGetDTO(venueEntity: VenueEntity): VenueGetDTO
    fun mapToVenueSlimDTO(venueEntity: VenueEntity): VenueSlimDTO
    fun mapToVenueSearchDTOs(venueEntities: List<VenueEntity>): List<VenueSearchDTO>
    fun updateVenueEntity(venueUpdateDTO: VenueUpdateDTO, @MappingTarget venueEntity: VenueEntity): VenueEntity
}