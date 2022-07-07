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
    fun venueEntityToVenueDTONoReviews(venueEntity: VenueEntity): VenueDTONoReviews
    fun venueDTONoReviewsToVenueEntity(venueDTONoReviews: VenueDTONoReviews): VenueEntity
    fun venueDTOIdNameToVenueEntity(venueDTOIdName: VenueDTOIdName): VenueEntity
    fun venueEntityToVenueDTOIdName(venueEntity: VenueEntity): VenueDTOIdName
    fun venueEntitiesToVenueDTONoReviews(venueEntities: List<VenueEntity>): List<VenueDTONoReviews>
    fun updateVenueEntityFromVenueDTONoReviews(
        venueDTONoReviews: VenueDTONoReviews,
        @MappingTarget venueEntity: VenueEntity
    ): VenueEntity
}