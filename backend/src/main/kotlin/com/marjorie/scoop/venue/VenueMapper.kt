package com.marjorie.scoop.venue

import com.marjorie.scoop.venue.dto.VenueDTONoReviews
import com.marjorie.scoop.venue.dto.VenueDTOIdName
import com.marjorie.scoop.venue.dto.VenueDTO
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
interface VenueMapper {
    fun venueEntityToVenueDTO(venueEntity: VenueEntity): VenueDTO
    fun venueDTOToVenueEntity(venueDTO: VenueDTO): VenueEntity
    fun venueEntityToSimpleVenueDTO(venueEntity: VenueEntity): VenueDTONoReviews
    fun simpleVenueDTOToVenueEntity(venueDTONoReviews: VenueDTONoReviews): VenueEntity
    fun simplestVenueDTOToVenueEntity(venueDTOIdName: VenueDTOIdName): VenueEntity
    fun venueEntityToSimplestVenueDTO(venueEntity: VenueEntity): VenueDTOIdName
    fun venueEntitiesToSimpleVenueDTOs(venueEntities: List<VenueEntity>): List<VenueDTONoReviews>
    fun updateVenueEntityFromSimpleVenueDTO(
        venueDTONoReviews: VenueDTONoReviews,
        @MappingTarget venueEntity: VenueEntity
    ): VenueEntity
}