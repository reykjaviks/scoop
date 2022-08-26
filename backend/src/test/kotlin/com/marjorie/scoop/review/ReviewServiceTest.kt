package com.marjorie.scoop.review

import com.marjorie.scoop.auth.user.UserEntity
import com.marjorie.scoop.auth.user.dto.UserSlimDTO
import com.marjorie.scoop.common.ScoopBadRequestException
import com.marjorie.scoop.common.ScoopResourceNotFoundException
import com.marjorie.scoop.review.dto.ReviewDTO
import com.marjorie.scoop.review.dto.ReviewPostDTO
import com.marjorie.scoop.review.dto.ReviewUpdateDTO
import com.marjorie.scoop.venue.VenueEntity
import com.marjorie.scoop.venue.VenueService
import com.marjorie.scoop.venue.dto.VenueSlimDTO
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.test.context.support.WithMockUser
import java.time.Instant
import kotlin.test.assertFailsWith

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class ReviewServiceTest {
    final var reviewRepository: ReviewRepository = mockk()
    final val reviewMapper: ReviewMapper = mockk()
    final var venueService: VenueService = mockk()
    var reviewService = ReviewService(reviewRepository, reviewMapper, venueService)

    lateinit var reviewDTO1: ReviewDTO
    lateinit var reviewDTO2: ReviewDTO
    lateinit var reviewEntity1: ReviewEntity
    lateinit var reviewEntity2: ReviewEntity
    lateinit var reviewPostDTO: ReviewPostDTO

    @BeforeEach
    fun setUp() {
        this.initTestData()
    }

    @Test
    fun `getReview returns a review when queried ID exists`() {
        val id: Long = 1

        every { reviewRepository.findByIdOrNull(id) } returns reviewEntity1
        every { reviewMapper.mapToReviewDTO(any()) } returns reviewDTO1

        val expectedReview = reviewEntity1.review
        val actualReview = reviewService.getReview(id)!!.review

        verify(exactly = 1) { reviewRepository.findByIdOrNull(id) };
        assertEquals(expectedReview, actualReview)
    }

    @Test
    fun `getReview returns null when queried ID does not exist`() {
        val id: Long = 2

        every { reviewRepository.findByIdOrNull(id) } returns null

        val reviewDTO = reviewService.getReview(id)

        verify(exactly = 1) { reviewRepository.findByIdOrNull(id) }
        assertNull(reviewDTO)
    }

    @Test
    fun `getAllReviews returns a list of reviews`() {
        every { reviewRepository.findAll() } returns listOf(reviewEntity1, reviewEntity2)
        every { reviewMapper.mapToReviewDTOs(any()) } returns listOf(reviewDTO1, reviewDTO2)

        val expectedList = listOf(reviewDTO1, reviewDTO2)
        val actualList = reviewService.getAllReviews()

        verify(exactly = 1) { reviewRepository.findAll() }
        assertEquals(expectedList, actualList)
    }

    @Test
    fun `createReview saves review`() {
        every { reviewRepository.save(any()) } returns reviewEntity1
        every { venueService.venueExists(reviewPostDTO.venueId) } returns true
        every { reviewMapper.mapToReviewEntity(reviewPostDTO) } returns reviewEntity1
        every { reviewMapper.mapToReviewDTO(any()) } returns reviewDTO1

        val savedReview = reviewService.createReview(reviewPostDTO)
        val expectedReview = reviewDTO1

        assertEquals(expectedReview, savedReview)
    }

    @Test
    fun `createReview does not save review when venue does not exist`() {
        every { venueService.venueExists(reviewPostDTO.venueId) } returns false

        assertFailsWith(
            exceptionClass = ScoopResourceNotFoundException::class,
            block = { reviewService.createReview(reviewPostDTO) }
        )
    }

    @Test
    @WithMockUser(username="Marjorie", authorities = ["ROLE_USER", "ROLE_ADMIN"])
    fun `updateReview updates review`() {
        every { reviewRepository.findByIdOrNull(any()) } returns reviewEntity1
        every { reviewMapper.updateReviewEntity(any(), any()) } returns reviewEntity1
        every { reviewMapper.mapToReviewDTO(reviewEntity1) } returns reviewDTO1

        val updatedVenue = reviewService.updateReview(1, ReviewUpdateDTO(review = "Test"))
        val expectedVenue = reviewDTO1

        assertEquals(expectedVenue, updatedVenue)
    }

    @Test
    @WithMockUser(username="Marjorie", authorities = ["ROLE_USER", "ROLE_ADMIN"])
    fun `updateReview doesn't update the review if venue doesn't exist`() {
        every { reviewRepository.findByIdOrNull(any()) } returns null

        assertFailsWith(
            exceptionClass = ScoopResourceNotFoundException::class,
            block = { reviewService.updateReview(1, ReviewUpdateDTO(review = "Test")) }
        )
    }

    @Test
    @WithMockUser(username="Hacker")
    fun `updateReview doesn't update the review if review writer is different than auth user`() {
        every { reviewRepository.findByIdOrNull(any()) } returns reviewEntity1

        assertFailsWith(
            exceptionClass = IllegalAccessException::class,
            block = { reviewService.updateReview(1, ReviewUpdateDTO(review = "Test")) }
        )
    }

    @Test
    @WithMockUser(username="Marjorie")
    fun `updateReview doesn't update the review if review and rating are both null`() {
        every { reviewRepository.findByIdOrNull(any()) } returns reviewEntity1

        assertFailsWith(
            exceptionClass = ScoopBadRequestException::class,
            block = { reviewService.updateReview(1, ReviewUpdateDTO(review = null, rating = null)) }
        )
    }

    private fun initTestData() {
        val marjorie = UserEntity(
            name = "Marjorie Moore",
            username = "Marjorie",
            password = "12345",
        )

        val slimMarjorie = UserSlimDTO(
            id = 1,
            name = "Marjorie",
            username = "marjorie@mail.com"

        )

        val wingery = VenueEntity(
            name = "Pretty Boy Wingery",
            streetAddress = "Piispansilta 11",
            postalCode = "02230",
            city = "Espoo",
        )

        reviewEntity1 = ReviewEntity(
            review = "It was okay, I guess.",
            rating = 3.0,
            venue = wingery,
            user = marjorie,
        )

        reviewEntity2 = ReviewEntity(
            review = "I appreciated how the staff was friendly without being overly chatty.",
            rating = 4.0,
            venue = wingery,
            user = marjorie,
        )

        reviewDTO1 = ReviewDTO(
            id = 1,
            review = "It was okay, I guess.",
            rating = 3.0,
            venue = VenueSlimDTO(id = 1, name = "Test place"),
            user = slimMarjorie,
            createdAt = Instant.now()
        )

        reviewDTO2= ReviewDTO(
            id = 2,
            review = "I appreciated how the staff was friendly without being overly chatty.",
            rating = 4.0,
            venue = VenueSlimDTO(id = 1, name = "Test place"),
            user = slimMarjorie,
            createdAt = Instant.now()
        )

        reviewPostDTO = ReviewPostDTO(
            review = "It was okay, I guess.",
            rating = 3.0,
            venueId = 1,
            username = marjorie.username
        )
    }
}