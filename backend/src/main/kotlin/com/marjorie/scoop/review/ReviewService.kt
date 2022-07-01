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
    fun createReview(reviewDTO: ReviewDTO): Review {
        val venue = reviewDTO.venueId?.let { venueService.getVenue(it) }
        val user = userService.getUser(reviewDTO.writer)
        when {
            venue == null -> throw KotlinNullPointerException("could not find a venue associated with the id '${reviewDTO.venueId}'")
            user == null -> throw KotlinNullPointerException("could not find a user associated with username '${reviewDTO.writer}'")
            else -> return reviewRepository.save(
                Review(
                    review = reviewDTO.review,
                    rating = reviewDTO.rating,
                    venue = venue,
                    user = user
                )
            )
        }
    }

    @PreAuthorize("#reviewDTO.writer == authentication.name")
    fun updateReview(id: Long, reviewDTO: ReviewDTO) {
        reviewRepository.updateReviewById(reviewDTO.review, reviewDTO.rating, id)
    }
}