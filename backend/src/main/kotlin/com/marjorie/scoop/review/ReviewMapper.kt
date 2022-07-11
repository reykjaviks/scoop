package com.marjorie.scoop.review

import com.marjorie.scoop.auth.user.UserService
import com.marjorie.scoop.review.dto.ReviewDTO
import com.marjorie.scoop.review.dto.ReviewUpdateDTO
import com.marjorie.scoop.review.dto.ReviewSlimDTO
import com.marjorie.scoop.review.dto.ReviewPostDTO
import com.marjorie.scoop.venue.VenueService
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy

@Mapper(
    componentModel = "spring",
    uses = [VenueService::class, UserService::class],
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
interface ReviewMapper {
    fun mapToReviewDTO(reviewEntity: ReviewEntity): ReviewDTO
    fun mapToReviewDTOs(reviewEntities: List<ReviewEntity>): List<ReviewDTO>
    fun mapToReviewEntity(reviewDTO: ReviewDTO): ReviewEntity
    fun mapToReviewEntity(reviewSlimDTO: ReviewSlimDTO): ReviewEntity
    @Mapping(source = "venueId", target = "venue")
    @Mapping(source = "username", target = "user")
    fun mapToReviewEntity(reviewPostDTO: ReviewPostDTO): ReviewEntity
    fun mapToReviewSlimDTO(reviewEntity: ReviewEntity): ReviewSlimDTO
    fun updateReviewEntity(reviewUpdateDTO: ReviewUpdateDTO, @MappingTarget reviewEntity: ReviewEntity): ReviewEntity
}