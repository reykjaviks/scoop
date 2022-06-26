package com.marjorie.scoop.review

import com.marjorie.scoop.auth.user.UserService
import com.marjorie.scoop.venue.VenueService
import org.springframework.data.repository.findByIdOrNull
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
    fun getAllReviews(): List<Review?> = reviewRepository.findAll()
    fun addReview(reviewDTO: ReviewDTO) {
        val venue = venueService.getVenue(reviewDTO.venueId)
        val user = userService.getUser(reviewDTO.writer)
        if (venue != null && user != null) {
            reviewRepository.save(
                Review(
                    review = reviewDTO.review,
                    rating = reviewDTO.rating,
                    venue = venue,
                    user = user
                )
            )
        } else throw KotlinNullPointerException("Can't save review to the database because venue or user is null.")
    }
}