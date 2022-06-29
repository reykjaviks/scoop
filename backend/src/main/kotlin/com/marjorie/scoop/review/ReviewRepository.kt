package com.marjorie.scoop.review

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional


/**
 * API for basic CRUD operations on Review
 */
@Repository
@Transactional
interface ReviewRepository: JpaRepository<Review?, Long?> {
    @Modifying
    @Query("update Review r set r.review = :review, r.rating = :rating where r.id = :reviewId")
    fun updateReviewById(review: String, rating: Double, reviewId: Long)
}