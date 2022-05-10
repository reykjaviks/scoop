package com.marjorie.scoop.venue

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext

@SpringBootTest
internal class VenueControllerTest {

    @MockkBean
    private lateinit var venueService: VenueService

    @Autowired
    private lateinit var venueController: VenueController

    private lateinit var venue1: Venue
    private lateinit var venue2: Venue
    private lateinit var venue3: Venue
    @BeforeEach
    fun setUp(context: ApplicationContext) {
        venue1 = Venue("Pretty Boy Wingery")
        venue2 = Venue("More Tea")
        venue3 = Venue("Pastis")

        every { venueService.getVenue(1).get() } returns venue1
        every { venueService.getVenue(2).get() } returns venue2
        every { venueService.getVenue(3).get() } returns venue3

        every { venueService.getVenue(4).isPresent } returns false
    }

    @Test
    fun `Venue controller returns a venue if the venue exists`() {
        assertThat(venueController.getVenue(1).get()).isEqualTo(venue1)
        verify { venueService.getVenue(1) }
    }

    @Test
    fun `Venue controller returns null if id does not exist`() {
        assertThat(venueController.getVenue(4)).isEmpty
        verify { venueService.getVenue(4) }
    }

}