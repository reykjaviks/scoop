package com.marjorie.scoop.venue

import com.fasterxml.jackson.databind.ObjectMapper
import com.marjorie.scoop.common.Constants.CSRF_IDENTIFIER
import com.marjorie.scoop.common.Constants.REQUEST_ID
import com.marjorie.scoop.neighbourhood.NeighbourhoodDTO
import com.marjorie.scoop.venue.dto.VenueDTONoReviews
import com.marjorie.scoop.venue.dto.VenueDTO
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
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

    @Autowired
    lateinit var objectMapper: ObjectMapper

    lateinit var tapiolaDTO: VenueDTO
    lateinit var kallioDTO: VenueDTO
    lateinit var simpleTapiolaDTO: VenueDTONoReviews
    lateinit var simpleKallioDTO: VenueDTONoReviews
    lateinit var simpleWackyDTO: VenueDTONoReviews

    val kallioQuery = "kallio"
    val wackyQuery = "qwerty1234"
    val requestId = "01_01_001"
    val csrfIdentifier = "scoop-client"

    @BeforeEach
    fun setUp() {
        this.initTestData()
        every { venueService.getVenueNew(1) } returns tapiolaDTO
        every { venueService.getVenueNew(2) } returns kallioDTO
        every { venueService.getVenueNew(3) } returns null
        every { venueService.getAllVenues() } returns listOf(simpleTapiolaDTO, simpleKallioDTO)
        every { venueService.searchVenues(kallioQuery) } returns listOf(simpleKallioDTO)
        every { venueService.searchVenues(wackyQuery) } returns null
        every { venueService.createVenue(simpleKallioDTO) } returns kallioDTO
        every { venueService.createVenue(simpleTapiolaDTO) } returns null
        every { venueService.updateVenue(1, simpleTapiolaDTO) } returns tapiolaDTO
        every { venueService.updateVenue(3, simpleWackyDTO) } returns null
    }

    @Test
    fun `Get venue returns a venue when queried ID exists`() {
        mockMvc.perform(get("/api/venue/1")
            .header(REQUEST_ID, requestId)
            .header(CSRF_IDENTIFIER, csrfIdentifier)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().string(containsString(tapiolaDTO.name)))
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
                    simpleKallioDTO.name,
                    simpleTapiolaDTO.name
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
                    simpleKallioDTO.neighbourhood?.name,
                    simpleTapiolaDTO.neighbourhood?.name
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
            .andExpect(jsonPath("$[*].name").value(contains(simpleKallioDTO.name)))
            .andExpect(jsonPath("$[*].name").value(not(contains(simpleTapiolaDTO.name))))
    }

    @Test
    fun `Search returns status code 404 Not Found when there are no results for the query`() {
        mockMvc.perform(get("/api/venue/search")
            .queryParam("query", wackyQuery)
            .header(REQUEST_ID, requestId)
            .header(CSRF_IDENTIFIER, csrfIdentifier)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isNotFound)
    }

    @Test
    @WithMockUser(username="Marjorie", authorities = ["ROLE_USER", "ROLE_ADMIN"])
    fun `Create venue returns status code 201 Created when new venue is created`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/venue/add")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .header(REQUEST_ID, requestId)
            .header(CSRF_IDENTIFIER, csrfIdentifier)
            .content(objectMapper.writeValueAsString(simpleKallioDTO))
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isCreated)
    }

    @Test
    @WithMockUser(username="Marjorie", authorities = ["ROLE_USER", "ROLE_ADMIN"])
    fun `Create venue returns status code 209 Conflict when venue already exists`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/venue/add")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .header(REQUEST_ID, requestId)
                .header(CSRF_IDENTIFIER, csrfIdentifier)
                .content(objectMapper.writeValueAsString(simpleTapiolaDTO))
                .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isConflict)
    }

    @Test
    @WithMockUser(username="Marjorie", authorities = ["ROLE_USER", "ROLE_ADMIN"])
    fun `Update venue returns status code 200 OK when an existing venue is updated`() {
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/venue/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .header(REQUEST_ID, requestId)
                .header(CSRF_IDENTIFIER, csrfIdentifier)
                .content(objectMapper.writeValueAsString(simpleTapiolaDTO))
                .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isOk)
    }

    @Test
    @WithMockUser(username="Marjorie", authorities = ["ROLE_USER", "ROLE_ADMIN"])
    fun `Update venue returns status code 404 Not Found when the venue does not exist`() {
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/venue/3")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .header(REQUEST_ID, requestId)
                .header(CSRF_IDENTIFIER, csrfIdentifier)
                .content(objectMapper.writeValueAsString(simpleWackyDTO))
                .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isNotFound)
    }

    private fun initTestData() {
        tapiolaDTO = VenueDTO(
            name = "Pretty Boy Wingery",
            streetAddress = "Piispansilta 11",
            postalCode = "02230",
            city = "Espoo",
            neighbourhood = NeighbourhoodDTO(name = "Tapiola"),
        )

        kallioDTO = VenueDTO(
            name = "Momochi",
            streetAddress = "Mannerheimintie 20",
            postalCode = "00100",
            city = "Helsinki",
            neighbourhood = NeighbourhoodDTO(name = "Kallio"),
        )

        simpleTapiolaDTO = VenueDTONoReviews(
            name = "Pretty Boy Wingery",
            streetAddress = "Piispansilta 11",
            postalCode = "02230",
            city = "Espoo",
            neighbourhood = NeighbourhoodDTO(name = "Tapiola"),
        )

        simpleKallioDTO = VenueDTONoReviews(
            name = "Momochi",
            streetAddress = "Mannerheimintie 20",
            postalCode = "00100",
            city = "Helsinki",
            neighbourhood = NeighbourhoodDTO(name = "Kallio"),
        )

        simpleWackyDTO = VenueDTONoReviews(
            name = "Wacky-Venue",
            streetAddress = "Nowhere",
            postalCode = "77777",
            city = "Imagiland"
        )
    }
}