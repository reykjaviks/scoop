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
    fun venueEntityToSimpleVenueDTO(venueEntity: VenueEntity): SimpleVenueDTO
    fun simpleVenueDTOToVenueEntity(simpleVenueDTO: SimpleVenueDTO): VenueEntity
    fun venueEntitiesToSimpleVenueDTOs(venueEntities: List<VenueEntity>): List<SimpleVenueDTO>
}