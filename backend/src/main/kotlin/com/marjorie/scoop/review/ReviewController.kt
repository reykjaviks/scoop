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
    fun getReview(@PathVariable id: Long): Review? {
        return reviewService.getReview(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "No review found for id $id")
    }

    @GetMapping("/all")
    fun getAllReviews(): Iterable<Review?> = reviewService.getAllReviews()

    @PostMapping("/add")
    fun addReview(@RequestBody reviewDTO: ReviewDTO) = reviewService.addReview(reviewDTO)
}