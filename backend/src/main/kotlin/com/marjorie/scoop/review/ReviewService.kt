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
    fun getReview(id: Long): Review? = reviewRepository.findByIdOrNull(id)

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    fun getAllReviews(): List<Review?> = reviewRepository.findAll()

    @PreAuthorize("#reviewDTO.writer == authentication.name")
    fun addReview(reviewDTO: ReviewDTO): Review {
        val venue = reviewDTO.venueId?.let { venueService.getVenue(it) }
        val user = userService.getUser(reviewDTO.writer)
        return if (venue != null && user != null) {
            reviewRepository.save(
                Review(
                    review = reviewDTO.review,
                    rating = reviewDTO.rating,
                    venue = venue,
                    user = user
                )
            )
        } else throw KotlinNullPointerException("Can't save the review because venue or user is null.")
    }

    @PreAuthorize("#reviewDTO.writer == authentication.name")
    fun updateReview(id: Long, reviewDTO: ReviewDTO) {
        reviewRepository.updateReviewById(reviewDTO.review, reviewDTO.rating, id)
    }
}