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

    private lateinit var venueEntity1: VenueEntity
    private lateinit var venueEntity2: VenueEntity

    @BeforeEach
    fun setUp() {
        venueEntity1 = VenueEntity(
            name = "Pretty Boy Wingery",
            streetAddress = "Piispansilta 11",
            postalCode = "02230",
            city = "Espoo",
            neighbourhood = Neighbourhood("Tapiola"),
        )

        venueEntity2 = VenueEntity(
            name = "Pastis",
            streetAddress = "Pieni Roobertinkatu 2",
            postalCode = "00130",
            city = "Helsinki",
            neighbourhood = Neighbourhood("Kaartinkaupunki"),
        )

        every { venueRepository.findByIdOrNull(1) } returns venueEntity1
        every { venueRepository.findByIdOrNull(2) } returns venueEntity2
        every { venueRepository.findByIdOrNull(3) } returns null
        every { venueRepository.findAll() } returns listOf(venueEntity1, venueEntity2)
        every {
            venueRepository.findByNameOrAddressOrPostalCodeOrCityOrNeighbourhood("%kaartinkaupunki%")
        } returns listOf(venueEntity2)
        every {
            venueRepository.findByNameOrAddressOrPostalCodeOrCityOrNeighbourhood("%reykjavik%")
        } returns null
    }

    @Test
    fun `getVenue returns a venue when queried ID exists`() {
        val venueId: Long = 1
        val expectedName = venueEntity1.name
        val actualName = venueService.getVenue(venueId)!!.name

        verify(exactly = 1) { venueRepository.findByIdOrNull(venueId) };
        assertEquals(expectedName, actualName)
    }

    @Test
    fun `getVenue returns null when queried ID does not exist`() {
        val venueId: Long = 3
        val venueEntity: VenueEntity? = venueService.getVenue(venueId)

        verify(exactly = 1) { venueRepository.findByIdOrNull(venueId) };
        assertNull(venueEntity)
    }

    @Test
    fun `getAllVenus returns a list of venues`() {
        val expectedList = listOf(venueEntity1, venueEntity2)
        val actualList = venueService.getAllVenues()

        verify(exactly = 1) { venueRepository.findAll() }
        assertEquals(expectedList, actualList)
    }

    @Test
    fun `searchVenues returns a list of venues located in the queried neighbourhood `() {
        val query = "KAARTINKAUPUNKI"
        val expectedVenues = listOf(venueEntity2)
        val actualVenue = venueService.searchVenues(query)

        assertEquals(expectedVenues, actualVenue)
    }

    @Test
    fun `Result of searchVenues does not contain a venue located in a different city`() {
        val query = "KAARTINKAUPUNKI"
        val venues = venueService.searchVenues(query)

        venues?.let { assertFalse(it.contains(venueEntity1)) }
    }

    @Test
    fun `searchVenues return null if there is no venues that match the query`() {
        val query = "REYKJAVIK"
        val venues = venueService.searchVenues(query)

        assertNull(venues)
    }
}