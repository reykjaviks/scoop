package com.marjorie.scoop.review

import com.marjorie.scoop.common.ScoopBadRequestException
import com.marjorie.scoop.common.ScoopResourceNotFoundException
import com.marjorie.scoop.review.dto.ReviewDTO
import com.marjorie.scoop.review.dto.ReviewPostDTO
import com.marjorie.scoop.review.dto.ReviewUpdateDTO
import com.marjorie.scoop.venue.VenueService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

/**
 * Handles the communication between Review repository and Review controller.
 */
@Service
class ReviewService(
    private val reviewRepository: ReviewRepository,
    private val reviewMapper: ReviewMapper,
    private val venueService: VenueService,
) {
    fun getReview(id: Long): ReviewDTO? {
        val reviewEntity = reviewRepository.findByIdOrNull(id) ?: return null
        return reviewMapper.mapToReviewDTO(reviewEntity)
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    fun getAllReviews(): List<ReviewDTO>? {
        val allReviewEntities = reviewRepository.findAll() as List<ReviewEntity>
        if (allReviewEntities.isNotEmpty()) {
            return reviewMapper.mapToReviewDTOs(allReviewEntities)
        }
        return null
    }

    @PreAuthorize("#reviewPostDTO.username == authentication.name")
    fun createReview(reviewPostDTO: ReviewPostDTO): ReviewDTO {
        if (!venueService.venueExists(reviewPostDTO.venueId)) {
            throw ScoopResourceNotFoundException("Could not find a venue associated with the ID ${reviewPostDTO.venueId}")
        }
        val savedReview = reviewRepository.save(reviewMapper.mapToReviewEntity(reviewPostDTO))
        return reviewMapper.mapToReviewDTO(savedReview)
    }

    fun updateReview(reviewId: Long, reviewUpdateDTO: ReviewUpdateDTO): ReviewDTO {
        val reviewEntity = this.getReviewEntity(reviewId)
        if (reviewEntity == null) {
            throw ScoopResourceNotFoundException("No review found for ID $reviewId")
        } else if (!this.isAuthUserAllowedToEdit(reviewEntity))  {
            throw IllegalAccessException("Not allowed to update other users' reviews")
        } else if (reviewUpdateDTO.review == null && reviewUpdateDTO.rating == null) {
            throw ScoopBadRequestException("Review and rating are both null")
        }
        val updatedReviewEntity = reviewMapper.updateReviewEntity(reviewUpdateDTO, reviewEntity)
        return reviewMapper.mapToReviewDTO(updatedReviewEntity)
    }

    private fun isAuthUserAllowedToEdit(review: ReviewEntity): Boolean {
        val authUser = SecurityContextHolder.getContext().authentication
        return authUser.name == review.user.username
    }

    private fun getReviewEntity(id: Long): ReviewEntity? {
        return reviewRepository.findByIdOrNull(id)
    }
}