package com.marjorie.scoop.review

import com.marjorie.scoop.venue.Venue
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*

/**
 * Exposes endpoints that serve information on reviews.
 */
@RestController
@RequestMapping("/api/review")
class ReviewController(private val reviewService: ReviewService) {
    @GetMapping("/all")
    fun listReviews(): Iterable<Review?> = reviewService.getReviews()

    @GetMapping("/{id}")
    fun getReview(@PathVariable id: Long): Review? {
        return reviewService.getReview(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "No review found for id $id")
    }
}