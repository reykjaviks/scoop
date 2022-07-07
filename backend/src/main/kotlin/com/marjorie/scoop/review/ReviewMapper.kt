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
    fun mapToReviewDTO(reviewEntity: ReviewEntity): ReviewDTO
    fun mapToReviewDTOList(reviewEntities: List<ReviewEntity>): List<ReviewDTO>
    fun mapToReviewEntity(reviewDTO: ReviewDTO): ReviewEntity
    fun mapToReviewEntity(reviewDTONoVenue: ReviewDTONoVenue): ReviewEntity
    fun mapToReviewDTONoVenue(reviewEntity: ReviewEntity): ReviewDTONoVenue
    fun updateReviewEntity(reviewDTOUpdate: ReviewDTOUpdate, @MappingTarget reviewEntity: ReviewEntity): ReviewEntity
}