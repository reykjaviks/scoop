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

    private lateinit var tapiolaVenue: Venue
    private lateinit var kallioVenue: Venue

    private lateinit var allVenues: List<Venue>
    private lateinit var kallioVenues: List<Venue>

    private lateinit var kallioQuery: String
    private lateinit var wackyQuery: String

    @BeforeEach
    fun setUp() {
        tapiolaVenue = Venue(
            name = "Pretty Boy Wingery",
            streetAddress = "Piispansilta 11",
            postalCode = "02230",
            city = "Espoo",
            neighbourhood = Neighbourhood("Tapiola")
        )

        kallioVenue = Venue(
            name = "Momochi",
            streetAddress = "Mannerheimintie 20",
            postalCode = "00100",
            city = "Helsinki",
            neighbourhood = Neighbourhood("Kallio")
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
        webClient.get()
            .uri("/api/venue/1")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(Venue::class.java)
    }

    @Test
    fun `Get venue returns status code 404 when queried ID does not exist`() {
        webClient.get()
            .uri("/api/venue/3")
            .exchange()
            .expectStatus().isNotFound
            .expectBody<Unit>()
    }

    @Test
    fun `Get all venues returns at least two results with correct name fields`() {
        webClient.get()
            .uri("/api/venue/all")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$[0].name").isEqualTo(tapiolaVenue.name)
            .jsonPath("$[1].name").isEqualTo(kallioVenue.name)
    }

    @Test
    fun `Get all venues returns at least two results with correct neighbourhood name fields`() {
        webClient.get()
            .uri("/api/venue/all")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$[0].neighbourhood.name").isEqualTo(tapiolaVenue.neighbourhood!!.name)
            .jsonPath("$[1].neighbourhood.name").isEqualTo(kallioVenue.neighbourhood!!.name)
    }
    @Test
    fun `API returns the right amount of venues`() {
        webClient.get()
            .uri("/api/venue/all")
            .exchange()
            .expectStatus().isOk
            .expectBodyList<Venue>()
            .hasSize(allVenues.size)
            //.contains(venue1) //todo: find out why this does not work
    }

    @Test
    fun `Search returns a list of venues located in Kallio`() {
        val query = "KALLIO"
        webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/api/venue/search")
                    .queryParam("query", query)
                    .build()
            }.exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$[0].name").isEqualTo(kallioVenue.name)
    }

    @Test
    fun `Search returns status code 404 when there are no results for the query`() {
        val query = "QWERTY1234"
        webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/api/venue/search")
                    .queryParam("query", query)
                    .build()
            }.exchange()
            .expectStatus().isNotFound
            .expectBody<Unit>()
    }

}