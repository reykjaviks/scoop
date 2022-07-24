package com.marjorie.scoop.review

import com.fasterxml.jackson.databind.ObjectMapper
import com.marjorie.scoop.auth.user.dto.UserSlimDTO
import com.marjorie.scoop.common.Constants.CSRF_IDENTIFIER
import com.marjorie.scoop.common.Constants.REQUEST_ID
import com.marjorie.scoop.review.dto.ReviewDTO
import com.marjorie.scoop.review.dto.ReviewPostDTO
import com.marjorie.scoop.review.dto.ReviewUpdateDTO
import com.marjorie.scoop.venue.dto.*
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.Instant

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
internal class ReviewControllerTest {
    @MockkBean lateinit var reviewService: ReviewService
    @Autowired lateinit var mockMvc: MockMvc
    @Autowired lateinit var objectMapper: ObjectMapper

    lateinit var reviewDTO1: ReviewDTO
    lateinit var reviewDTO2: ReviewDTO
    lateinit var reviewPostDTO: ReviewPostDTO

    val requestId = "01_01_001"
    val csrfIdentifier = "scoop-client"

    @BeforeEach
    fun setUp() {
        this.initTestData()
    }

    @Test
    fun `Get review returns a venue when queried ID exists`() {
        val id: Long = 1

        every { reviewService.getReview(id) } returns reviewDTO1

        mockMvc.perform(get("/api/review/".plus(id))
            .header(REQUEST_ID, requestId)
            .header(CSRF_IDENTIFIER, csrfIdentifier)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().string(containsString(reviewDTO1.review)))
    }

    @Test
    fun `Get review returns status code 404 when queried ID does not exist`() {
        val id: Long = 1

        every { reviewService.getReview(id) } returns null

        mockMvc.perform(get("/api/review/".plus(id))
            .header(REQUEST_ID, requestId)
            .header(CSRF_IDENTIFIER, csrfIdentifier)
            .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound)
    }

    @Test
    fun `Get all reviews returns at least two results with correct review fields`() {
        every { reviewService.getAllReviews() } returns listOf(reviewDTO1, reviewDTO2)

        mockMvc.perform(get("/api/review/all")
            .header(REQUEST_ID, requestId)
            .header(CSRF_IDENTIFIER, csrfIdentifier)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[*].review").value(
                containsInAnyOrder(
                    reviewDTO1.review,
                    reviewDTO2.review
                )
            ))
    }

    @Test
    fun `Get all reviews returns at least two results with correct rating fields`() {
        every { reviewService.getAllReviews() } returns listOf(reviewDTO1, reviewDTO2)

        mockMvc.perform(get("/api/review/all")
            .header(REQUEST_ID, requestId)
            .header(CSRF_IDENTIFIER, csrfIdentifier)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[*].rating").value(
                containsInAnyOrder(
                    reviewDTO1.rating,
                    reviewDTO2.rating
                )
            ))
    }

    @Test
    @WithMockUser
    fun `Create review return status code 201 when review is saved`() {
        every { reviewService.createReview(reviewPostDTO) } returns reviewDTO1

        mockMvc.perform(post("/api/review/add")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .header(REQUEST_ID, requestId)
            .header(CSRF_IDENTIFIER, csrfIdentifier)
            .content(objectMapper.writeValueAsString(reviewPostDTO))
            .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated)
    }

    @Test
    fun `Create review returns 401 Unauthorized when user is not authenticated`() {
        mockMvc.perform(post("/api/review/add")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .header(REQUEST_ID, requestId)
            .header(CSRF_IDENTIFIER, csrfIdentifier)
        ).andExpect(status().isUnauthorized)
    }

    @Test
    @WithMockUser
    fun `Update review returns status code 200 when review is updated`() {
        val id: Long = 1
        val updateDTO = ReviewUpdateDTO(review = "I have changed my mind. It was good overall.")

        every { reviewService.updateReview(id, updateDTO) } returns reviewDTO1

        mockMvc.perform(patch("/api/review/".plus(id))
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .header(REQUEST_ID, requestId)
            .header(CSRF_IDENTIFIER, csrfIdentifier)
            .content(objectMapper.writeValueAsString(updateDTO))
            .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
    }

    @Test
    fun `Create review returns 401 Unauthorized when auth user and review user don't match`() {
        val postDTO = ReviewPostDTO(review = "Ok.", rating = 2.0, username = "Marjorie", venueId = 1)

        mockMvc.perform(post("/api/review/add")
            .with(httpBasic("Marjorie", "12345"))
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .header(REQUEST_ID, requestId)
            .header(CSRF_IDENTIFIER, csrfIdentifier)
            .content(objectMapper.writeValueAsString(postDTO))
            .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized)
    }

    private fun initTestData() {
        val username = "Marjorie"

        reviewDTO1 = ReviewDTO(
            id = 1,
            review = "It was okay, I guess.",
            rating = 3.0,
            venue = VenueSlimDTO(id = 1, name = "Test place"),
            user = UserSlimDTO(1, username = username),
            createdAt = Instant.now()
        )

        reviewDTO2 = ReviewDTO(
            id = 2,
            review = "I appreciated how the staff was friendly without being overly chatty.",
            rating = 4.0,
            venue = VenueSlimDTO(id = 1, name = "Test place"),
            user = UserSlimDTO(1, username = username),
            createdAt = Instant.now()
        )

        reviewPostDTO = ReviewPostDTO(
            review = "It was okay, I guess.",
            rating = 3.0,
            venueId = 1,
            username = username
        )
    }
}