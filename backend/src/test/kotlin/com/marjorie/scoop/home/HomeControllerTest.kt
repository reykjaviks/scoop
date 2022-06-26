package com.marjorie.scoop.home

import com.marjorie.scoop.common.Constants.CSRF_IDENTIFIER
import com.marjorie.scoop.common.Constants.REQUEST_ID
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
class HomeControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `Home contains a greeting message`() {
        mockMvc
            .perform(get("/")
                .header(REQUEST_ID, "request-id-500")
                .header(CSRF_IDENTIFIER, "csrf-identifier-600")
            )
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(
                content().string(containsString("Welcome to Scoop"))
            )
    }
}