package com.marjorie.scoop.venue

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VenueControllerTest2 {

    @Test
    fun shouldReturnGreeting(@Autowired webClient: WebTestClient) {
        webClient.get()
            .uri("/api/venue/all")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody<String>()
            //.isEqualTo("Hello Foo!")
    }

}