package com.marjorie.scoop.review

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

/**
 * Handles the communication between Review repository and Review controller.
 */
@Service
class ReviewService(
    private val reviewRepository: ReviewRepository,
) {
    fun getReview(id: Long): Review? = reviewRepository.findByIdOrNull(id)
    fun getAllReviews(): List<Review?> = reviewRepository.findAll()
    fun addReview(review: Review) = reviewRepository.save(review)
}