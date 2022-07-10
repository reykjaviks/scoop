package com.marjorie.scoop.review

import com.marjorie.scoop.common.ScoopBadRequestException
import com.marjorie.scoop.common.ScoopResourceNotFoundException
import com.marjorie.scoop.review.dto.ReviewDTO
import com.marjorie.scoop.review.dto.ReviewPostDTO
import com.marjorie.scoop.review.dto.ReviewUpdateDTO
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
    fun createReview(@RequestBody reviewPostDTO: ReviewPostDTO): ReviewDTO {
        try {
            return reviewService.createReview(reviewPostDTO)
        } catch (e: ScoopResourceNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Error in creating a review: ${e.message}")
        }
    }

    @PatchMapping("/{reviewId}")
    fun updateReview(@PathVariable reviewId: Long, @RequestBody reviewUpdateDTO: ReviewUpdateDTO): ReviewDTO {
        try {
            return reviewService.updateReview(reviewId, reviewUpdateDTO)
        } catch (e: ScoopResourceNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "${e.message}")
        } catch (e: ScoopBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "${e.message}")
        } catch (e: IllegalAccessException) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "${e.message}")
        }
    }

}