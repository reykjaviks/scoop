package com.marjorie.scoop.review

import com.marjorie.scoop.auth.user.User
import com.marjorie.scoop.auth.user.UserService
import com.marjorie.scoop.venue.Venue
import com.marjorie.scoop.venue.VenueService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReviewServiceTest {
    private var reviewRepository: ReviewRepository = mockk()
    private var venueService: VenueService = mockk()
    private var userService: UserService = mockk()
    private var reviewService = ReviewService(reviewRepository, venueService, userService)

    private lateinit var review1_DTO: ReviewDTO
    private lateinit var review2_DTO: ReviewDTO
    private lateinit var review1: Review
    private lateinit var review2: Review

    @BeforeEach
    fun setUp() {
        this.initTestData()
        every { reviewRepository.findByIdOrNull(1) } returns review1
        every { reviewRepository.findByIdOrNull(2) } returns review2
        every { reviewRepository.findByIdOrNull(3) } returns null
        every { reviewRepository.findAll() } returns listOf(review1, review2)
        every { reviewRepository.save(review1) } returns review1
    }

    @Test
    fun `getReview returns a review when queried ID exists`() {
        val reviewId: Long = 1
        val expectedReview = review1.review
        val actualReview = reviewService.getReview(reviewId)!!.review

        verify(exactly = 1) { reviewRepository.findByIdOrNull(reviewId) };
        assertEquals(expectedReview, actualReview)
    }

    @Test
    fun `getReview returns null when queried ID does not exist`() {
        val reviewId: Long = 3
        val review: Review? = reviewService.getReview(reviewId)

        verify(exactly = 1) { reviewRepository.findByIdOrNull(reviewId) };
        assertNull(review)
    }

    @Test
    fun `getAllReviews returns a list of reviews`() {
        val expectedList = listOf(review1, review2)
        val actualList: List<Review?> = reviewService.getAllReviews()

        verify(exactly = 1) { reviewRepository.findAll() };
        assertEquals(expectedList, actualList)
    }

    @Test
    fun `addReview saves review`() {
        // todo: implement later
    }

    @Test
    fun `addReview does not save review`() {
        // todo: implement later
    }

    private fun initTestData() {
        val venue1_id: Long = 67

        val userMarjorie = User(
            name = "Marjorie Moore",
            username = "Marjorie",
            password = "12345",
        )

        val userEleanor = User(
            name = "Eleanor M.",
            username = "Eleanor",
            password = "12345",
        )

        val venue1 = Venue(
            name = "Pretty Boy Wingery",
            streetAddress = "Piispansilta 11",
            postalCode = "02230",
            city = "Espoo",
        )

        review1 = Review(
            review = "It was okay, I guess.",
            rating = 3.0,
            venue = venue1,
            user = userMarjorie,
        )

        review2 = Review(
            review = "I appreciated how the staff was friendly without being overly chatty.",
            rating = 4.0,
            venue = venue1,
            user = userEleanor,
        )

        review1_DTO = ReviewDTO(
            review = "It was okay, I guess.",
            rating = 3.0,
            venueId = venue1_id,
            writer = userMarjorie.username,
        )

        review2_DTO= ReviewDTO(
            review = "I appreciated how the staff was friendly without being overly chatty.",
            rating = 4.0,
            venueId = venue1_id,
            writer = userEleanor.username,
        )
    }
}