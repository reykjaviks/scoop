package com.marjorie.scoop

import org.hamcrest.Matchers.containsString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Tests HTTP requests without starting a server.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class WebApplicationTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `Home contains a greeting message`() {
        mockMvc
            .perform(get("/").header("request-id", "randomID"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(
                content().string(containsString("Welcome to Scoop"))
            )
    }
}