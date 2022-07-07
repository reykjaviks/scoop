package com.marjorie.scoop.venue

import com.marjorie.scoop.neighbourhood.NeighbourhoodService
import com.marjorie.scoop.venue.dto.VenueDTO
import com.marjorie.scoop.venue.dto.VenueDTOIdName
import com.marjorie.scoop.venue.dto.VenueDTONoReviews
import com.marjorie.scoop.venue.dto.VenueDTOPost
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
    @Mapping(source = "neighbourhoodId", target = "neighbourhood")
    fun mapToVenueEntity(venueDTOPost: VenueDTOPost): VenueEntity
    fun mapToVenueEntity(venueDTO: VenueDTO): VenueEntity
    fun mapToVenueEntity(venueDTOIdName: VenueDTOIdName): VenueEntity
    fun mapToVenueEntity(venueDTONoReviews: VenueDTONoReviews): VenueEntity
    fun mapToVenueDTO(venueEntity: VenueEntity): VenueDTO
    fun mapToVenueDTOIdName(venueEntity: VenueEntity): VenueDTOIdName
    fun mapToVenueDTONoReviews(venueEntity: VenueEntity): VenueDTONoReviews
    fun mapToVenueDTONoReviewsList(venueEntities: List<VenueEntity>): List<VenueDTONoReviews>
    fun updateVenueEntity(venueDTONoReviews: VenueDTONoReviews, @MappingTarget venueEntity: VenueEntity): VenueEntity
}