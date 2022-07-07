package com.marjorie.scoop.review

import com.marjorie.scoop.auth.user.UserService
import com.marjorie.scoop.review.dto.ReviewDTO
import com.marjorie.scoop.review.dto.ReviewDTOPost
import com.marjorie.scoop.review.dto.ReviewDTOUpdate
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
    private val userService: UserService,
) {
    fun getReview(id: Long): ReviewDTO? {
        val reviewEntity = reviewRepository.findByIdOrNull(id)
        return if (reviewEntity == null) {
            null
        } else {
            reviewMapper.mapToReviewDTO(reviewEntity)
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    fun getAllReviews(): List<ReviewDTO>? {
        val allReviewEntities = reviewRepository.findAll() as List<ReviewEntity>
        return if (allReviewEntities.isEmpty()) {
            null
        } else {
            return reviewMapper.mapToReviewDTOList(allReviewEntities)
        }
    }

    // todo: fix to use mapper
    @PreAuthorize("#reviewDTOPost.username == authentication.name")
    fun createReview(reviewDTOPost: ReviewDTOPost): ReviewDTO {
        val venueEntity = venueService.getVenueEntity(reviewDTOPost.venueId)
        val userEntity = userService.getUser(reviewDTOPost.username)
        if (venueEntity == null) {
            throw KotlinNullPointerException("Could not find a venue associated with the ID ${reviewDTOPost.venueId}")
        } else {
            val reviewEntity = reviewRepository.save(
                ReviewEntity(
                    review = reviewDTOPost.review,
                    rating = reviewDTOPost.rating,
                    venue = venueEntity,
                    user = userEntity!!
                )
            )
            return reviewMapper.mapToReviewDTO(reviewEntity)
        }
    }

    fun updateReview(reviewId: Long, reviewDTOUpdate: ReviewDTOUpdate): ReviewDTO {
        val reviewEntity = reviewRepository.findByIdOrNull(reviewId)
        if (reviewEntity == null) {
            throw KotlinNullPointerException("No review found for ID $reviewId")
        } else if (!this.isAuthUserAllowedToEdit(reviewEntity))  {
            throw IllegalAccessException("Not allowed to update other users' reviews")
        } else if (reviewDTOUpdate.review == null && reviewDTOUpdate.rating == null) {
            throw KotlinNullPointerException("Can't update a review when both review and rating are null")
        }
        val updatedReviewEntity = reviewMapper.updateReviewEntity(reviewDTOUpdate, reviewEntity)
        return reviewMapper.mapToReviewDTO(updatedReviewEntity)
    }

    private fun isAuthUserAllowedToEdit(review: ReviewEntity): Boolean {
        val authUser = SecurityContextHolder.getContext().authentication
        return authUser.principal == review.user.username
    }
}