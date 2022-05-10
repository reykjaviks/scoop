package com.marjorie.scoop.venue

import com.ninjasquad.springmockk.MockkBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class VenueControllerTestOld {

    @MockkBean
    private lateinit var venueService: VenueService

    @Autowired
    private lateinit var venueController: VenueController

    private lateinit var venue1: Venue
    private lateinit var venue2: Venue
    private lateinit var venue3: Venue
    /*
    @BeforeEach
    fun setUp(context: ApplicationContext) {
        venue1 = Venue("Pretty Boy Wingery")
        venue2 = Venue("More Tea")
        venue3 = Venue("Pastis")

        every { venueService.getVenue(1) } returns venue1
        every { venueService.getVenue(2) } returns venue2
        every { venueService.getVenue(3) } returns venue3

        every { venueService.getVenue(4).isPresent } returns false
    }

    @Test
    fun `Venue controller returns a venue if the venue exists`() {
        assertThat(venueController.getVenue(1)).isEqualTo(venue1)
        verify { venueService.getVenue(1) }
    }

    @Test
    fun `Venue controller returns null if id does not exist`() {
        assertThat(venueController.getVenue(4)).isNull()
        verify { venueService.getVenue(4) }
    }
    **/

}