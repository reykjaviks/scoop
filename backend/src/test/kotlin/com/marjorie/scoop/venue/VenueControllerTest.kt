package com.marjorie.scoop.venue

import com.fasterxml.jackson.databind.ObjectMapper
import com.marjorie.scoop.common.Constants.CSRF_IDENTIFIER
import com.marjorie.scoop.common.Constants.REQUEST_ID
import com.marjorie.scoop.common.ScoopResourceAlreadyExistsException
import com.marjorie.scoop.neighbourhood.dto.NeighbourhoodGetDTO
import com.marjorie.scoop.venue.dto.VenueGetDTO
import com.marjorie.scoop.venue.dto.VenuePostDTO
import com.marjorie.scoop.venue.dto.VenueSearchDTO
import com.marjorie.scoop.venue.dto.VenueUpdateDTO
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
import java.time.Instant

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
internal class VenueControllerTest {
    @MockkBean lateinit var venueService: VenueService
    @Autowired lateinit var mockMvc: MockMvc
    @Autowired lateinit var objectMapper: ObjectMapper

    lateinit var tapiolaDTO: VenueGetDTO
    lateinit var kallioDTO: VenueGetDTO
    lateinit var tapiolaSearchDTO: VenueSearchDTO
    lateinit var kallioSearchDTO: VenueSearchDTO
    lateinit var postDTO: VenuePostDTO

    val requestId = "01_01_001"
    val csrfIdentifier = "scoop-client"

    @BeforeEach
    fun setUp() {
        this.initTestData()
    }

    @Test
    fun `Get venue returns a venue when queried ID exists`() {
        val id: Long = 1

        every { venueService.getVenueDTO(id) } returns tapiolaDTO

        mockMvc.perform(get("/api/venue/".plus(id))
            .header(REQUEST_ID, requestId)
            .header(CSRF_IDENTIFIER, csrfIdentifier)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().string(containsString(tapiolaDTO.name)))
    }

    @Test
    fun `Get venue returns status code 404 when queried ID does not exist`() {
        val id: Long = 3

        every { venueService.getVenueDTO(id) } returns null

        mockMvc.perform(get("/api/venue/".plus(id))
            .header(REQUEST_ID, requestId)
            .header(CSRF_IDENTIFIER, csrfIdentifier)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isNotFound)
    }

    @Test
    fun `Get all venues returns at least two results with correct name fields`() {
        every { venueService.getAllVenues() } returns listOf(kallioSearchDTO, tapiolaSearchDTO)

        mockMvc.perform(get("/api/venue/all")
            .header(REQUEST_ID, requestId)
            .header(CSRF_IDENTIFIER, csrfIdentifier)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[*].name").value(
                containsInAnyOrder(
                    kallioSearchDTO.name,
                    tapiolaSearchDTO.name
                )
            ))
    }

    @Test
    fun `Get all venues returns at least two results with correct neighbourhood fields`() {
        every { venueService.getAllVenues() } returns listOf(kallioSearchDTO, tapiolaSearchDTO)

        mockMvc.perform(get("/api/venue/all")
            .header(REQUEST_ID, requestId)
            .header(CSRF_IDENTIFIER, csrfIdentifier)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[*].neighbourhood.name").value(
                containsInAnyOrder(
                    kallioSearchDTO.neighbourhood?.name,
                    tapiolaSearchDTO.neighbourhood?.name
                )
            ))
    }

    @Test
    fun `Search returns a list of venues located in Kallio`() {
        val kallioQuery = "kallio"

        every { venueService.searchVenues(kallioQuery) } returns listOf(kallioSearchDTO)

        mockMvc.perform(get("/api/venue/search")
            .queryParam("query", kallioQuery)
            .header(REQUEST_ID, requestId)
            .header(CSRF_IDENTIFIER, csrfIdentifier)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[*].name").value(contains(kallioSearchDTO.name)))
            .andExpect(jsonPath("$[*].name").value(not(contains(tapiolaSearchDTO.name))))
    }

    @Test
    fun `Search returns status code 404 Not Found when there are no results for the query`() {
        val query = "Jali's Chocolate Factory"

        every { venueService.searchVenues(any()) } returns null

        mockMvc.perform(get("/api/venue/search")
            .queryParam("query", query)
            .header(REQUEST_ID, requestId)
            .header(CSRF_IDENTIFIER, csrfIdentifier)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isNotFound)
    }

    @Test
    @WithMockUser(username="Marjorie", authorities = ["ROLE_USER", "ROLE_ADMIN"])
    fun `Create venue returns status code 201 Created when new venue is created`() {
        every { venueService.createVenue(postDTO) } returns tapiolaDTO

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/venue/add")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .header(REQUEST_ID, requestId)
            .header(CSRF_IDENTIFIER, csrfIdentifier)
            .content(objectMapper.writeValueAsString(postDTO))
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isCreated)
    }

    @Test // todo: fix
    @WithMockUser(username="Marjorie", authorities = ["ROLE_USER", "ROLE_ADMIN"])
    fun `Create venue returns status code 209 Conflict when venue already exists`() {
        every { venueService.createVenue(postDTO) } throws Exception("This is a test")

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/venue/add")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .header(REQUEST_ID, requestId)
                .header(CSRF_IDENTIFIER, csrfIdentifier)
                .content(objectMapper.writeValueAsString(postDTO))
                .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isConflict)
    }

    @Test
    @WithMockUser(username="Marjorie", authorities = ["ROLE_USER", "ROLE_ADMIN"])
    fun `Update venue returns status code 200 OK when an existing venue is updated`() {
        val id: Long = 1
        val updateDTO = VenueUpdateDTO(name = "My name is new")

        every { venueService.updateVenue(id, updateDTO) } returns tapiolaDTO

        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/venue/".plus(id))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .header(REQUEST_ID, requestId)
                .header(CSRF_IDENTIFIER, csrfIdentifier)
                .content(objectMapper.writeValueAsString(updateDTO))
                .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isOk)
    }

    @Test // todo: fix
    @WithMockUser(username="Marjorie", authorities = ["ROLE_USER", "ROLE_ADMIN"])
    fun `Update venue returns status code 404 Not Found when the venue does not exist`() {
        val updateDTO = VenueUpdateDTO()

        every { venueService.updateVenue(any(), any()) }.throws(ScoopResourceAlreadyExistsException("Resource already exists"))

        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/venue/3")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .header(REQUEST_ID, requestId)
                .header(CSRF_IDENTIFIER, csrfIdentifier)
                .content(objectMapper.writeValueAsString(updateDTO))
                .accept(MediaType.APPLICATION_JSON)
        ).andDo(print())
            .andExpect(status().isNotFound)
    }

    private fun initTestData() {
        tapiolaDTO = VenueGetDTO(
            id = 1,
            name = "Pretty Boy Wingery",
            streetAddress = "Piispansilta 11",
            postalCode = "02230",
            city = "Espoo",
            neighbourhood = NeighbourhoodGetDTO(id = 1, name = "Tapiola"),
            createdAt = Instant.now()
        )

        kallioDTO = VenueGetDTO(
            id = 2,
            name = "Momochi",
            streetAddress = "Mannerheimintie 20",
            postalCode = "00100",
            city = "Helsinki",
            neighbourhood = NeighbourhoodGetDTO(id = 2, name = "Kallio"),
            createdAt = Instant.now()
        )

        tapiolaSearchDTO = VenueSearchDTO(
            id = 1,
            name = "Pretty Boy Wingery",
            streetAddress = "Piispansilta 11",
            postalCode = "02230",
            city = "Espoo",
            neighbourhood = NeighbourhoodGetDTO(id = 1, name = "Tapiola"),
            createdAt = Instant.now()
        )

        kallioSearchDTO = VenueSearchDTO(
            id = 2,
            name = "Momochi",
            streetAddress = "Mannerheimintie 20",
            postalCode = "00100",
            city = "Helsinki",
            neighbourhood = NeighbourhoodGetDTO(id = 2, name = "Kallio"),
            createdAt = Instant.now()
        )

        postDTO = VenuePostDTO(
            name = "Pretty Boy Wingery",
            streetAddress = "Piispansilta 11",
            postalCode = "02230",
            city = "Espoo",
        )
    }
}