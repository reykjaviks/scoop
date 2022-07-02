package com.marjorie.scoop.venue

import com.marjorie.scoop.venue.VenueDTO
import com.marjorie.scoop.venue.VenueEntity
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
interface VenueMapper {
    fun venueEntityToVenueDTO(venueEntity: VenueEntity): VenueDTO
    fun venueDTOToVenueEntity(venueDTO: VenueDTO): VenueEntity
}