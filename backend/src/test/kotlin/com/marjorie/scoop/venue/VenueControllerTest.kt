package com.marjorie.scoop.venue

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

//@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class VenueControllerTest {

    @MockkBean
    private lateinit var venueService: VenueService

    @Autowired
    private lateinit var venueController: VenueController

    @Test
    fun `Venue controller returns a venue if id exists`() {
        val venue1 = Venue("Pretty Boy Wingery")
        every { venueService.getVenue(1).get() } returns venue1
        assertThat(venueController.getVenue(1).get().name).isEqualTo("Pretty Boy Wingery")
        verify { venueService.getVenue(1) }
    }

    @Test
    fun `Venue controller returns null if id does not exist`() {
        every { venueService.getVenue(2).isPresent } returns false
        assertThat(venueController.getVenue(2)).isEmpty
        verify { venueService.getVenue(2) }
    }

}