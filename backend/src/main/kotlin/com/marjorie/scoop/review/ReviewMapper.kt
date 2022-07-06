package com.marjorie.scoop.review

import com.marjorie.scoop.review.dto.ReviewDTO
import com.marjorie.scoop.review.dto.ReviewDTOUpdate
import com.marjorie.scoop.review.dto.ReviewDTONoVenue
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
interface ReviewMapper {
    fun reviewEntityToReviewDTO(reviewEntity: ReviewEntity): ReviewDTO
    fun reviewDTOToReviewEntity(reviewDTO: ReviewDTO): ReviewEntity
    fun reviewEntityToSimplerReviewDTO(reviewEntity: ReviewEntity): ReviewDTONoVenue
    fun simplerReviewDTOToReviewEntity(reviewDTONoVenue: ReviewDTONoVenue): ReviewEntity
    fun reviewEntitiesToReviewDTOs(reviewEntities: List<ReviewEntity>): List<ReviewDTO>
    fun updateReviewEntityFromReviewUpdateDTO(
        reviewDTOUpdate: ReviewDTOUpdate,
        @MappingTarget reviewEntity: ReviewEntity,
    ): ReviewEntity
}