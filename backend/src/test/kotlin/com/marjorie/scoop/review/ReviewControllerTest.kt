package com.marjorie.scoop.review

import com.fasterxml.jackson.databind.ObjectMapper
import com.marjorie.scoop.auth.user.User
import com.marjorie.scoop.common.Constants.CSRF_IDENTIFIER
import com.marjorie.scoop.common.Constants.REQUEST_ID
import com.marjorie.scoop.venue.VenueEntity
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
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ReviewControllerTest {
    @MockkBean
    lateinit var reviewService: ReviewService

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    lateinit var reviewEntity1: ReviewEntity
    lateinit var reviewEntity2: ReviewEntity
    lateinit var user: User

    val requestId = "01_01_001"
    val csrfIdentifier = "scoop-client"

    @BeforeEach
    fun setUp() {
        this.initTestData()
        every { reviewService.getReview(1) } returns reviewEntity1
        every { reviewService.getReview(2) } returns reviewEntity1
        every { reviewService.getReview(3) } returns null
        every { reviewService.getAllReviews() } returns listOf(reviewEntity1, reviewEntity2)
        every { reviewService.createReview(any()) } returns reviewEntity1
        every { reviewService.updateReview(any(), any()) } returns Unit
    }

    @Test
    fun `Get review returns a venue when queried ID exists`() {
        mockMvc.perform(get("/api/review/1")
            .header(REQUEST_ID, requestId)
            .header(CSRF_IDENTIFIER, csrfIdentifier)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().string(containsString(reviewEntity1.review)))
    }

    @Test
    fun `Get review returns status code 404 when queried ID does not exist`() {
        mockMvc.perform(get("/api/review/3")
            .header(REQUEST_ID, requestId)
            .header(CSRF_IDENTIFIER, csrfIdentifier)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isNotFound)
    }

    @Test
    fun `Get all reviews returns at least two results with correct review fields`() {
        mockMvc.perform(get("/api/review/all")
            .header(REQUEST_ID, requestId)
            .header(CSRF_IDENTIFIER, csrfIdentifier)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[*].review").value(
                containsInAnyOrder(
                    reviewEntity1.review,
                    reviewEntity2.review
                )
            ))
    }

    @Test
    fun `Get all reviews returns at least two results with correct rating fields`() {
        mockMvc.perform(get("/api/review/all")
            .header(REQUEST_ID, requestId)
            .header(CSRF_IDENTIFIER, csrfIdentifier)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[*].rating").value(
                containsInAnyOrder(
                    reviewEntity1.rating,
                    reviewEntity2.rating
                )
            ))
    }

    @Test
    @WithMockUser(username="Marjorie", authorities = ["ROLE_USER", "ROLE_ADMIN"])
    fun `Create review return status code 201 when review is saved`() {
        val data = ReviewData(
            review = "It was okay, I guess.",
            rating = 3.0,
            venueId = 1,
            writer = "Marjorie",
        )
        mockMvc.perform(post("/api/review/add")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .header(REQUEST_ID, requestId)
            .header(CSRF_IDENTIFIER, csrfIdentifier)
            .content(objectMapper.writeValueAsString(data))
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isCreated)
    }

    @Test
    @WithMockUser(username="Marjorie", authorities = ["ROLE_USER", "ROLE_ADMIN"])
    fun `Update review returns status code 200 when review is updated`() {
        val data = ReviewData(
            review = "It was okay, I guess.",
            rating = 3.0,
            writer = "Marjorie",
        )
        mockMvc.perform(
            patch("/api/review/1")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .header(REQUEST_ID, requestId)
            .header(CSRF_IDENTIFIER, csrfIdentifier)
            .content(objectMapper.writeValueAsString(data))
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isOk)
    }

    @Test
    @WithMockUser(username="Marjorie", authorities = ["ROLE_USER", "ROLE_ADMIN"])
    fun `Update review returns status code 200 (success) when review is updated`() {
        val data = ReviewData(
            review = "New review",
            rating = 1.0,
            writer = "Marjorie"
        )
        mockMvc.perform(
            patch("/api/review/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .header(REQUEST_ID, requestId)
                .header(CSRF_IDENTIFIER, csrfIdentifier)
                .content(objectMapper.writeValueAsString(data))
                .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isOk)
    }

    private fun initTestData() {
        val venueEntity = VenueEntity(
            name = "Pretty Boy Wingery",
            streetAddress = "Piispansilta 11",
            postalCode = "02230",
            city = "Espoo",
        )

        user = User(
            name = "Marjorie Moore",
            username = "Marjorie",
            password = "12345",
        )

        reviewEntity1 = ReviewEntity(
            review = "It was okay, I guess.",
            rating = 3.0,
            venueEntity = venueEntity,
            user = user
        )

        reviewEntity2 = ReviewEntity(
            review = "I appreciated how the staff was friendly without being overly chatty.",
            rating = 4.0,
            venueEntity = venueEntity,
            user = user
        )
    }
}