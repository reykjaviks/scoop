package com.marjorie.scoop.venue

import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
interface VenueMapper {
    fun venueEntityToVenueDTO(venueEntity: VenueEntity): VenueDTO
    fun venueDTOToVenueEntity(venueDTO: VenueDTO): VenueEntity
    fun venueEntitiesToSimpleVenueDTOs(venueEntities: List<VenueEntity>): List<SimpleVenueDTO>
}