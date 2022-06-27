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
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class VenueControllerTest {
    @MockkBean
    private lateinit var venueRepository: VenueRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    private lateinit var tapiolaVenue: Venue
    private lateinit var kallioVenue: Venue
    private lateinit var allVenues: List<Venue>
    private lateinit var kallioVenues: List<Venue>
    private lateinit var kallioQuery: String
    private lateinit var wackyQuery: String

    private val requestId = "01_01_001"
    private val csrfIdentifier = "scoop-client"

    @BeforeEach
    fun setUp() {
        tapiolaVenue = Venue(
            name = "Pretty Boy Wingery",
            streetAddress = "Piispansilta 11",
            postalCode = "02230",
            city = "Espoo",
            neighbourhood = Neighbourhood("Tapiola"),
            reviewList = null,
        )

        kallioVenue = Venue(
            name = "Momochi",
            streetAddress = "Mannerheimintie 20",
            postalCode = "00100",
            city = "Helsinki",
            neighbourhood = Neighbourhood("Kallio"),
            reviewList = null,
        )

        allVenues = listOf(tapiolaVenue, kallioVenue)
        kallioVenues = listOf(kallioVenue)

        kallioQuery = "%kallio%"
        wackyQuery = "%qwerty1234%"

        every { venueRepository.findByIdOrNull(1) } returns tapiolaVenue
        every { venueRepository.findByIdOrNull(2) } returns kallioVenue
        every { venueRepository.findByIdOrNull(3) } returns null
        every { venueRepository.findAll() } returns allVenues
        every {
            venueRepository.findByNameOrAddressOrPostalCodeOrCityOrNeighbourhood(kallioQuery)
        } returns kallioVenues
        every {
            venueRepository.findByNameOrAddressOrPostalCodeOrCityOrNeighbourhood(wackyQuery)
        } returns null
    }

    @Test
    fun `Get venue returns a venue when queried ID exists`() {
        mockMvc.perform(get("/api/venue/1")
            .header(REQUEST_ID, requestId)
            .header(CSRF_IDENTIFIER, csrfIdentifier)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().string(containsString(tapiolaVenue.name)))
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
    fun `Get venue returns status code 400 when headers do not contain a request id`() {
        mockMvc.perform(get("/api/venue/1")
            .header(CSRF_IDENTIFIER, csrfIdentifier)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `Get venue returns status code 400 when headers do not contain a csrf identifier`() {
        mockMvc.perform(get("/api/venue/1")
            .header(REQUEST_ID, requestId)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isBadRequest)
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
                    tapiolaVenue.name,
                    kallioVenue.name
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
                    tapiolaVenue.neighbourhood!!.name,
                    kallioVenue.neighbourhood!!.name
                )
            ))
    }

    @Test
    fun `Search returns a list of venues located in Kallio`() {
        val query = "KALLIO"
        mockMvc.perform(get("/api/venue/search")
            .queryParam("query", query)
            .header(REQUEST_ID, requestId)
            .header(CSRF_IDENTIFIER, csrfIdentifier)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[*].name").value(contains(kallioVenue.name)))
            .andExpect(jsonPath("$[*].name").value(not(contains(tapiolaVenue.name))))
    }

    @Test
    fun `Search returns status code 404 when there are no results for the query`() {
        val query = "QWERTY1234"
        mockMvc.perform(get("/api/venue/search")
            .queryParam("query", query)
            .header(REQUEST_ID, requestId)
            .header(CSRF_IDENTIFIER, csrfIdentifier)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isNotFound)
    }
}