package com.marjorie.scoop.review

import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

/**
 * Exposes endpoints that serve information on reviews.
 */
@RestController
@RequestMapping("/api/review")
@Transactional
class ReviewController(private val reviewService: ReviewService) {
    @GetMapping("/{id}")
    fun getReview(@PathVariable id: Long): Review? {
        return reviewService.getReview(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "No review found for id $id")
    }

    @GetMapping("/all")
    fun getAllReviews(): Iterable<Review?> = reviewService.getAllReviews()

    @PostMapping("/add")
    fun createReview(@RequestBody reviewDTO: ReviewDTO): Review = try {
        reviewService.createReview(reviewDTO)
    } catch (npe: KotlinNullPointerException) {
        throw ResponseStatusException(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Error in adding a review: ${npe.message}"
        )
    }

    @PatchMapping("/{id}")
    fun updateReview(@PathVariable id: Long, @RequestBody reviewDTO: ReviewDTO) = reviewService.updateReview(id, reviewDTO)
}
