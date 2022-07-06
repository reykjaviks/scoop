package com.marjorie.scoop.review

import com.marjorie.scoop.review.dto.ReviewDTO
import com.marjorie.scoop.review.dto.ReviewDTOPost
import com.marjorie.scoop.review.dto.ReviewDTOUpdate
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
    @ResponseStatus(HttpStatus.OK)
    fun getReview(@PathVariable id: Long): ReviewDTO {
        return reviewService.getReview(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "No review found for id $id")
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    fun getAllReviews(): Iterable<ReviewDTO> {
        return reviewService.getAllReviews()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "The result of getting all reviews was empty")
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    fun createReview(@RequestBody reviewDTOPost: ReviewDTOPost): ReviewDTO {
        try {
            return reviewService.createReview(reviewDTOPost)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error in creating a review: ${e.message}")
        }
    }

    @PatchMapping("/{reviewId}")
    fun updateReview(@PathVariable reviewId: Long, @RequestBody reviewDTOUpdate: ReviewDTOUpdate): ReviewDTO {
        try {
            return reviewService.updateReview(reviewId, reviewDTOUpdate)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error in updating a review: ${e.message}")
        }
    }

}