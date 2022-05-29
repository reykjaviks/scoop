package com.marjorie.scoop.review

import org.springframework.stereotype.Service
import java.util.*

/**
 * Handles the communication between Review repository and Review controller.
 */
@Service
class ReviewService(private val reviewRepository : ReviewRepository) {
    fun getReviews(): List<Review?> = reviewRepository.findAll()
    fun getReview(id: Long): Optional<Review?> = reviewRepository.findById(id)
}