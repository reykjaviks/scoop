package com.marjorie.scoop.venue

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VenueControllerTest {

    @Test
    fun `API returns a venue when queried ID exists`(@Autowired webClient: WebTestClient) {
        webClient.get()
            .uri("/api/venue/67")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(Venue::class.java)
    }

    @Test
    fun `API returns status code 404 when queried ID does not exist`(@Autowired webClient: WebTestClient) {
        webClient.get()
            .uri("/api/venue/1")
            .exchange()
            .expectStatus().isNotFound
            .expectBody<Unit>()
    }

    @Test
    fun `API returns at least two results`(@Autowired webClient: WebTestClient) {
        webClient.get()
            .uri("/api/venue/all")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$[0].name").isNotEmpty
            .jsonPath("$[1].name").isNotEmpty
    }

}