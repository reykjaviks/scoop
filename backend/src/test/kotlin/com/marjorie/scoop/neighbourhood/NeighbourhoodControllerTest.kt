package com.marjorie.scoop.neighbourhood

import com.marjorie.scoop.common.Constants
import com.marjorie.scoop.neighbourhood.dto.NeighbourhoodGetDTO
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
internal class NeighbourhoodControllerTest {
    @MockkBean lateinit var neighbourhoodService: NeighbourhoodService
    @Autowired lateinit var mockMvc: MockMvc

    lateinit var kallioDTO: NeighbourhoodGetDTO
    lateinit var kluuviDTO: NeighbourhoodGetDTO

    val requestId = "01_01_001"
    val csrfIdentifier = "scoop-client"

    @BeforeEach
    fun setUp() {
        this.initTestData()
    }

    @Test
    fun `getNeighbourhood returns OK when queried ID exists`() {
        val id: Long = 1

        every { neighbourhoodService.getNeighbourhood(id) } returns kluuviDTO

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/neighbourhood/".plus(id))
            .header(Constants.REQUEST_ID, requestId)
            .header(Constants.CSRF_IDENTIFIER, csrfIdentifier)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(kluuviDTO.name)))
    }

    @Test
    fun `Get neighbourhood returns status code 404 when queried ID does not exist`() {
        val id: Long = 1

        every { neighbourhoodService.getNeighbourhood(id) } returns null

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/neighbourhood/".plus(id))
            .header(Constants.REQUEST_ID, requestId)
            .header(Constants.CSRF_IDENTIFIER, csrfIdentifier)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `Get all neighbourhoods returns at least two results with correct name fields`() {
        every { neighbourhoodService.getAllNeighbourhoods() } returns listOf(kluuviDTO, kallioDTO)

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/neighbourhood/all")
            .header(Constants.REQUEST_ID, requestId)
            .header(Constants.CSRF_IDENTIFIER, csrfIdentifier)
            .accept(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.jsonPath("$[*].name").value(
                    Matchers.containsInAnyOrder(
                        kluuviDTO.name,
                        kallioDTO.name
                    )
            ))
    }

    @Test
    fun `Get all neighbourhoods returns status code 404 Not Found not when neighbourhoods is null`() {
        every { neighbourhoodService.getAllNeighbourhoods() } returns null

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/neighbourhood/all")
                .header(Constants.REQUEST_ID, requestId)
                .header(Constants.CSRF_IDENTIFIER, csrfIdentifier)
                .accept(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    private fun initTestData() {
        kallioDTO = NeighbourhoodGetDTO(id = 1, name = "Kallio")
        kluuviDTO = NeighbourhoodGetDTO(id = 1, name = "Kluuvi")
    }
}