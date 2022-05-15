package com.marjorie.scoop.venue

import com.marjorie.scoop.neighbourhood.Neighbourhood
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
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

    @BeforeEach
    fun setUp() {
        venue1 = Venue(
            name = "Pretty Boy Wingery",
            streetAddress = "Piispansilta 11",
            postalCode = "02230",
            city = "Espoo",
        )

        venue2 = Venue(
            name = "Pastis",
            streetAddress = "Pieni Roobertinkatu 2",
            postalCode = "00130",
            city = "Helsinki",
            neighbourhood = Neighbourhood("Kaartinkaupunki")
        )

        every { venueRepository.findByIdOrNull(1) } returns venue1
        every { venueRepository.findByIdOrNull(2) } returns venue2
        every { venueRepository.findByIdOrNull(3) } returns null

        every { venueRepository.findAll() } returns listOf(venue1, venue2)

        every { venueRepository.findByNameOrAddressOrPostalCodeOrCityOrNeighbourhood(
            "%kaartinkaupunki%")
        } returns listOf(venue2)

        every { venueRepository.findByNameOrAddressOrPostalCodeOrCityOrNeighbourhood(
            "%reykjavik%")
        } returns null
    }

    @Test
    fun `getVenue returns a venue when queried ID exists`() {
        val venueId: Long = 1
        var resultVenue: Venue? = venueService.getVenue(venueId)

        verify(exactly = 1) { venueRepository.findByIdOrNull(venueId) };
        assertEquals(resultVenue?.name, venue1.name)
    }

    @Test
    fun `getVenue returns null when queried ID does not exist`() {
        val venueId: Long = 3
        var resultVenue: Venue? = venueService.getVenue(venueId)

        verify(exactly = 1) { venueRepository.findByIdOrNull(venueId) };
        assertNull(resultVenue)
    }

    @Test
    fun `searchVenues returns a list of venues located in the queried neighbourhood `() {
        val query = "KAARTINKAUPUNKI"
        val venues = venueService.searchVenues(query)

        assertEquals(venues, listOf(venue2))
    }

    @Test
    fun `result of searchVenues does not contain a venue located in a different city`() {
        val query = "KAARTINKAUPUNKI"
        val venues = venueService.searchVenues(query)

        venues?.let { assertFalse(it.contains(venue1)) }
    }

    @Test
    fun `searchVenues return null if there is no venues that match the query`() {
        val query = "REYKJAVIK"
        val venues = venueService.searchVenues(query)

        assertNull(venues)
    }

}