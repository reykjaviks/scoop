package com.marjorie.scoop.review

import com.marjorie.scoop.venue.Venue
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.web.bind.annotation.*
import java.util.*


/**
 * Exposes endpoints that serve information on reviews.
 * */
@RestController
@RequestMapping("/api/review")
class ReviewController(private val reviewService: ReviewService) {
    @GetMapping("/all")
    fun listReviews(): Iterable<Review?> = reviewService.getReviews()

    @GetMapping("/{id}")
    fun getReview(@PathVariable id: Long): Optional<Review?> = reviewService.getReview(id)
}