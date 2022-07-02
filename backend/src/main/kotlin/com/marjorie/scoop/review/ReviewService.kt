package com.marjorie.scoop.review

import com.marjorie.scoop.auth.user.UserService
import com.marjorie.scoop.venue.VenueService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service

/**
 * Handles the communication between Review repository and Review controller.
 */
@Service
class ReviewService(
    private val reviewRepository: ReviewRepository,
    private val venueService: VenueService,
    private val userService: UserService,
) {
    fun getReview(id: Long): ReviewEntity? = reviewRepository.findByIdOrNull(id)

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    fun getAllReviews(): List<ReviewEntity?> = reviewRepository.findAll()

    // todo: fix
    @PreAuthorize("#reviewData.writer == authentication.name")
    fun createReview(reviewData: ReviewData): ReviewEntity {
        val venue = reviewData.venueId?.let { venueService.getVenue(it) }
        val user = reviewData.writer?.let { userService.getUser(it) }
        if (venue == null)
            throw KotlinNullPointerException("Error in saving the review: could not find a venue with an id '${reviewData.venueId}'")
        else return reviewRepository.save(
            ReviewEntity(
                review = reviewData.review!!,
                rating = reviewData.rating!!,
                venueEntity = venue,
                user = user!!
            )
        )
    }

    @PreAuthorize("#reviewData.writer == authentication.name")
    fun updateReview(review: ReviewEntity, reviewData: ReviewData) {
        reviewData.review?.let { review.review = reviewData.review }
        reviewData.rating?.let { review.rating = reviewData.rating }
        reviewRepository.save(review)
    }
}