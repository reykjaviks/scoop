package com.marjorie.scoop.review

import com.marjorie.scoop.auth.user.User
import com.marjorie.scoop.auth.user.UserService
import com.marjorie.scoop.venue.VenueEntity
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
    var reviewRepository: ReviewRepository = mockk()
    var venueService: VenueService = mockk()
    var userService: UserService = mockk()
    var reviewService = ReviewService(reviewRepository, venueService, userService)

    lateinit var reviewData1: ReviewData
    lateinit var reviewData2: ReviewData
    lateinit var reviewEntity1: ReviewEntity
    lateinit var reviewEntity2: ReviewEntity

    lateinit var venueEntity: VenueEntity
    lateinit var userMarjorie: User

    var venueId: Long = 67
    var venueIdNonExisting: Long =  90

    @BeforeEach
    fun setUp() {
        this.initTestData()
        every { reviewRepository.findByIdOrNull(1) } returns reviewEntity1
        every { reviewRepository.findByIdOrNull(2) } returns reviewEntity2
        every { reviewRepository.findByIdOrNull(3) } returns null
        every { reviewRepository.findAll() } returns listOf(reviewEntity1, reviewEntity2)
        every { reviewRepository.save(any()) } returns reviewEntity1

        every { venueService.getVenue(venueId) } returns venueEntity
        every { venueService.getVenue(venueIdNonExisting) } returns null

        every { userService.getUser("Marjorie") } returns userMarjorie
    }

    @Test
    fun `getReview returns a review when queried ID exists`() {
        val reviewId: Long = 1
        val expectedReview = reviewEntity1.review
        val actualReview = reviewService.getReview(reviewId)!!.review

        verify(exactly = 1) { reviewRepository.findByIdOrNull(reviewId) };
        assertEquals(expectedReview, actualReview)
    }

    @Test
    fun `getReview returns null when queried ID does not exist`() {
        val reviewId: Long = 3
        val reviewEntity: ReviewEntity? = reviewService.getReview(reviewId)

        verify(exactly = 1) { reviewRepository.findByIdOrNull(reviewId) }
        assertNull(reviewEntity)
    }

    @Test
    fun `getAllReviews returns a list of reviews`() {
        val expectedList = listOf(reviewEntity1, reviewEntity2)
        val actualList: List<ReviewEntity?> = reviewService.getAllReviews()

        verify(exactly = 1) { reviewRepository.findAll() }
        assertEquals(expectedList, actualList)
    }

    @Test
    fun `createReview saves review`() {
        val savedReview = reviewService.createReview(reviewData1)
        val expectedReview = reviewEntity1

        assertEquals(expectedReview, savedReview)
    }

    @Test
    fun `createReview does not save review`() {
        val reviewData = ReviewData(review = "Cool place.", 1.0, venueId = null, writer = "Marjorie")
        val expectedMessage = "Error in saving the review: could not find a venue with an id 'null'"
        try {
            reviewService.createReview(reviewData)
        } catch (npe: KotlinNullPointerException) {
            assertEquals(expectedMessage, npe.message)
        }
    }

    private fun initTestData() {
        userMarjorie = User(
            name = "Marjorie Moore",
            username = "Marjorie",
            password = "12345",
        )

        venueEntity = VenueEntity(
            name = "Pretty Boy Wingery",
            streetAddress = "Piispansilta 11",
            postalCode = "02230",
            city = "Espoo",
        )

        reviewEntity1 = ReviewEntity(
            review = "It was okay, I guess.",
            rating = 3.0,
            venueEntity = venueEntity,
            user = userMarjorie,
        )

        reviewEntity2 = ReviewEntity(
            review = "I appreciated how the staff was friendly without being overly chatty.",
            rating = 4.0,
            venueEntity = venueEntity,
            user = userMarjorie,
        )

        reviewData1 = ReviewData(
            review = "It was okay, I guess.",
            rating = 3.0,
            venueId = venueId,
            writer = userMarjorie.username,
        )

        reviewData2= ReviewData(
            review = "I appreciated how the staff was friendly without being overly chatty.",
            rating = 4.0,
            venueId = venueIdNonExisting,
            writer = userMarjorie.username,
        )
    }
}