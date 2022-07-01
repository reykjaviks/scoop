package com.marjorie.scoop.review

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

/**
 * Exposes endpoints that serve information on reviews.
 */
@RestController
@RequestMapping("/api/review")
class ReviewController(private val reviewService: ReviewService) {
    @GetMapping("/{id}")
    fun getReview(@PathVariable id: Long): ReviewEntity? {
        return reviewService.getReview(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "No review found for id $id")
    }

    @GetMapping("/all")
    fun getAllReviews(): Iterable<ReviewEntity?> = reviewService.getAllReviews()

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    fun createReview(@RequestBody reviewData: ReviewData) {
        when {
            reviewData.review.isNullOrBlank() -> throw ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Invalid request: review can't be null or blank"
            )
            reviewData.rating == null || reviewData.rating.isNaN() -> throw ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Invalid request: rating can't be null or other than a number"
            )
            else -> reviewService.createReview(reviewData)
        }
    }

    @PatchMapping("/{id}")
    fun updateReview(@PathVariable id: Long, @RequestBody reviewData: ReviewData) {
        val review = reviewService.getReview(id)
        if (review == null) throw ResponseStatusException(HttpStatus.NOT_FOUND, "No review found for id $id")
        else reviewService.updateReview(review, reviewData)
    }
}