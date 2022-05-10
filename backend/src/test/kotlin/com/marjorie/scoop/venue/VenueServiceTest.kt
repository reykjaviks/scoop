package com.marjorie.scoop.venue

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VenueServiceTest {

    private var venueRepository: VenueRepository = mockk()
    private var venueService = VenueService(venueRepository)

    private lateinit var venue1: Venue
    private lateinit var venue2: Venue
    private lateinit var venue3: Venue

    @BeforeEach
    fun setUp() {
        venue1 = Venue("Pretty Boy Wingery")
        venue2 = Venue("More Tea")
        venue3 = Venue("Pastis")

        every { venueRepository.findByIdOrNull(1) } returns venue1
        every { venueRepository.findByIdOrNull(2) } returns venue2
        every { venueRepository.findByIdOrNull(3) } returns venue3
        every { venueRepository.findByIdOrNull(4) } returns null

        every { venueRepository.findAll() } returns listOf(venue1, venue2, venue3)
    }

    @Test
    fun `getVenue() returns a venue when queried ID exists`() {
        //  when
        var resultVenue: Venue? = venueService.getVenue(1)

        // then
        verify(exactly = 1) { venueRepository.findByIdOrNull(1) };
        assertEquals(resultVenue?.name, venue1.name)
    }

}