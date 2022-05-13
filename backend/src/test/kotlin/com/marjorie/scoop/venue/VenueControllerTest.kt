package com.marjorie.scoop.venue

import com.marjorie.scoop.neighbourhood.Neighbourhood
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.test.web.reactive.server.expectBodyList

@ExtendWith(SpringExtension::class)
@WebFluxTest(controllers = [VenueController::class])
@Import(VenueService::class)
class VenueControllerTest {

    @MockkBean
    private lateinit var venueRepository: VenueRepository

    @Autowired
    private lateinit var webClient: WebTestClient

    private lateinit var venue1: Venue
    private lateinit var venue2: Venue

    @BeforeEach
    fun setUp() {
        venue1 = Venue(
            name = "Pretty Boy Wingery",
            streetAddress = "Piispansilta 11",
            postalCode = "02230",
            city = "Espoo",
            neighbourhood = Neighbourhood("Tapiola")
        )

        venue2 = Venue(
            name = "Momochi",
            streetAddress = "Mannerheimintie 20",
            postalCode = "00100",
            city = "Helsinki",
            neighbourhood = Neighbourhood("Kallio")
        )

        every { venueRepository.findByIdOrNull(1) } returns venue1
        every { venueRepository.findByIdOrNull(2) } returns venue2
        every { venueRepository.findByIdOrNull(3) } returns null
        every { venueRepository.findAll() } returns listOf(venue1, venue2)
    }

    @Test
    fun `API returns a venue when queried ID exists`() {
        webClient.get()
            .uri("/api/venue/1")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(Venue::class.java)
    }

    @Test
    fun `API returns status code 404 when queried ID does not exist`() {
        webClient.get()
            .uri("/api/venue/3")
            .exchange()
            .expectStatus().isNotFound
            .expectBody<Unit>()
    }

    @Test
    fun `API returns at least two results with correct name fields`() {
        webClient.get()
            .uri("/api/venue/all")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$[0].name").isEqualTo(venue1.name)
            .jsonPath("$[1].name").isEqualTo(venue2.name)
    }

    @Test
    fun `API returns at least two results with correct neighbourhood name fields`() {
        webClient.get()
            .uri("/api/venue/all")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$[0].neighbourhood.name").isEqualTo("Tapiola")
            .jsonPath("$[1].neighbourhood.name").isEqualTo("Kallio")
    }
    @Test
    fun `API returns the right amount of venues`() {
        webClient.get()
            .uri("/api/venue/all")
            .exchange()
            .expectStatus().isOk
            .expectBodyList<Venue>()
            .hasSize(2)
            //.contains(venue1) //todo: find out why this does not work
    }

}