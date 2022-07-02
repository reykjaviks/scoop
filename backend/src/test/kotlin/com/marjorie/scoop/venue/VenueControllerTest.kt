package com.marjorie.scoop.venue

import com.marjorie.scoop.common.Constants.CSRF_IDENTIFIER
import com.marjorie.scoop.common.Constants.REQUEST_ID
import com.marjorie.scoop.neighbourhood.Neighbourhood
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
class VenueControllerTest {
    @MockkBean
    lateinit var venueService: VenueService

    @Autowired
    lateinit var mockMvc: MockMvc

    lateinit var tapiolaVenueEntity: VenueEntity
    lateinit var kallioVenueEntity: VenueEntity
    val kallioQuery = "kallio"
    val wackyQuery = "qwerty1234"
    val requestId = "01_01_001"
    val csrfIdentifier = "scoop-client"

    @BeforeEach
    fun setUp() {
        this.initTestData()
        every { venueService.getVenue(1) } returns tapiolaVenueEntity
        every { venueService.getVenue(2) } returns kallioVenueEntity
        every { venueService.getVenue(3) } returns null
        every { venueService.getAllVenues() } returns listOf(tapiolaVenueEntity, kallioVenueEntity)
        every { venueService.searchVenues(kallioQuery) } returns listOf(kallioVenueEntity)
        every { venueService.searchVenues(wackyQuery) } returns null
    }

    @Test
    fun `Get venue returns a venue when queried ID exists`() {
        mockMvc.perform(get("/api/venue/1")
            .header(REQUEST_ID, requestId)
            .header(CSRF_IDENTIFIER, csrfIdentifier)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().string(containsString(tapiolaVenueEntity.name)))
    }

    @Test
    fun `Get venue returns status code 404 when queried ID does not exist`() {
        mockMvc.perform(get("/api/venue/3")
            .header(REQUEST_ID, requestId)
            .header(CSRF_IDENTIFIER, csrfIdentifier)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isNotFound)
    }

    @Test
    fun `Get all venues returns at least two results with correct name fields`() {
        mockMvc.perform(get("/api/venue/all")
            .header(REQUEST_ID, requestId)
            .header(CSRF_IDENTIFIER, csrfIdentifier)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[*].name").value(
                containsInAnyOrder(
                    tapiolaVenueEntity.name,
                    kallioVenueEntity.name
                )
            ))
    }

    @Test
    fun `Get all venues returns at least two results with correct neighbourhood fields`() {
        mockMvc.perform(get("/api/venue/all")
            .header(REQUEST_ID, requestId)
            .header(CSRF_IDENTIFIER, csrfIdentifier)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[*].neighbourhood.name").value(
                containsInAnyOrder(
                    tapiolaVenueEntity.neighbourhood!!.name,
                    kallioVenueEntity.neighbourhood!!.name
                )
            ))
    }

    @Test
    fun `Search returns a list of venues located in Kallio`() {
        mockMvc.perform(get("/api/venue/search")
            .queryParam("query", kallioQuery)
            .header(REQUEST_ID, requestId)
            .header(CSRF_IDENTIFIER, csrfIdentifier)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[*].name").value(contains(kallioVenueEntity.name)))
            .andExpect(jsonPath("$[*].name").value(not(contains(tapiolaVenueEntity.name))))
    }

    @Test
    fun `Search returns status code 404 when there are no results for the query`() {
        mockMvc.perform(get("/api/venue/search")
            .queryParam("query", wackyQuery)
            .header(REQUEST_ID, requestId)
            .header(CSRF_IDENTIFIER, csrfIdentifier)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isNotFound)
    }

    private fun initTestData() {
        tapiolaVenueEntity = VenueEntity(
            name = "Pretty Boy Wingery",
            streetAddress = "Piispansilta 11",
            postalCode = "02230",
            city = "Espoo",
            neighbourhood = Neighbourhood("Tapiola"),
        )

        kallioVenueEntity = VenueEntity(
            name = "Momochi",
            streetAddress = "Mannerheimintie 20",
            postalCode = "00100",
            city = "Helsinki",
            neighbourhood = Neighbourhood("Kallio"),
        )
    }
}