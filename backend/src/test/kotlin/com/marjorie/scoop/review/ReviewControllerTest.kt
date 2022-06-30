package com.marjorie.scoop.review

import com.marjorie.scoop.auth.user.User
import com.marjorie.scoop.common.Constants.CSRF_IDENTIFIER
import com.marjorie.scoop.common.Constants.REQUEST_ID
import com.marjorie.scoop.venue.Venue
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ReviewControllerTest {
    @MockkBean
    private lateinit var reviewService: ReviewService

    @Autowired
    private lateinit var mockMvc: MockMvc

    private lateinit var review1: Review
    private lateinit var review2: Review
    private val requestId = "01_01_001"
    private val csrfIdentifier = "scoop-client"

    @BeforeEach
    fun setUp() {
        this.initTestData()
        every { reviewService.getReview(1) } returns review1
        every { reviewService.getReview(2) } returns review1
        every { reviewService.getReview(3) } returns null
        every { reviewService.getAllReviews() } returns listOf(review1, review2)
    }

    @Test
    fun `Get review returns a venue when queried ID exists`() {
        mockMvc.perform(get("/api/review/1")
            .header(REQUEST_ID, requestId)
            .header(CSRF_IDENTIFIER, csrfIdentifier)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().string(containsString(review1.review)))
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
                    review1.review,
                    review2.review
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
                    review1.rating,
                    review2.rating
                )
            ))
    }

    private fun initTestData() {
        val venue = Venue(
            name = "Pretty Boy Wingery",
            streetAddress = "Piispansilta 11",
            postalCode = "02230",
            city = "Espoo",
        )

        val user = User(
            name = "Marjorie Moore",
            username = "Marjorie",
            password = "12345",
        )

        review1 = Review(
            review = "It was okay, I guess.",
            rating = 3.0,
            venue = venue,
            user = user
        )

        review2 = Review(
            review = "I appreciated how the staff was friendly without being overly chatty.",
            rating = 4.0,
            venue = venue,
            user = user
        )
    }
}