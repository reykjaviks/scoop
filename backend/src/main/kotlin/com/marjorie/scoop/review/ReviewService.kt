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
        val review = reviewRepository.findByIdOrNull(id)
        return if (review == null) {
            null
        } else {
            reviewMapper.reviewEntityToReviewDTO(review)
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    fun getAllReviews(): List<ReviewDTO>? {
        val allReviews = reviewRepository.findAll() as List<ReviewEntity>
        return if (allReviews.isEmpty()) {
            null
        } else {
            return reviewMapper.reviewEntitiesToReviewDTOs(allReviews)
        }
    }

    @PreAuthorize("#reviewDTOPost.username == authentication.name")
    fun createReview(reviewDTOPost: ReviewDTOPost): ReviewDTO {
        val venue = venueService.getVenue(reviewDTOPost.venueId)
        val user = userService.getUser(reviewDTOPost.username)
        if (venue == null) {
            throw KotlinNullPointerException("Could not find a venue associated with the ID ${reviewDTOPost.venueId}")
        } else {
            val reviewEntity = reviewRepository.save(
                ReviewEntity(
                    review = reviewDTOPost.review,
                    rating = reviewDTOPost.rating,
                    venue = venue,
                    user = user!!
                )
            )
            return reviewMapper.reviewEntityToReviewDTO(reviewEntity)
        }
    }

    fun updateReview(reviewId: Long, reviewDTOUpdate: ReviewDTOUpdate): ReviewDTO {
        val review = reviewRepository.findByIdOrNull(reviewId)
        if (review == null) {
            throw KotlinNullPointerException("No review found for ID $reviewId")
        } else if (!this.isAuthUserAllowedToEdit(review))  {
            throw IllegalAccessException("Not allowed to update other users' reviews")
        } else if (reviewDTOUpdate.review == null && reviewDTOUpdate.rating == null) {
            throw KotlinNullPointerException("Can't update a review when both review and rating are null")
        }
        val updatedReviewEntity = reviewMapper.updateReviewEntityFromReviewDTOUpdate(reviewDTOUpdate, review)
        return reviewMapper.reviewEntityToReviewDTO(updatedReviewEntity)
    }

    private fun isAuthUserAllowedToEdit(review: ReviewEntity): Boolean {
        val authUser = SecurityContextHolder.getContext().authentication
        return authUser.principal == review.user.username
    }
}