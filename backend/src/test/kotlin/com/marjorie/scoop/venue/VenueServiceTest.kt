package com.marjorie.scoop.venue

import com.marjorie.scoop.neighbourhood.Neighbourhood
import com.marjorie.scoop.neighbourhood.NeighbourhoodDTO
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ContextConfiguration(classes = [VenueMapperImpl::class, ])
class VenueServiceTest {
    private val venueRepository: VenueRepository = mockk()
    private val venueMapper: VenueMapper = mockk()
    private val venueService = VenueService(venueRepository, venueMapper)

    private lateinit var prettyBoyWingeryDTO: VenueDTO
    private lateinit var prettyBoyWingery: VenueEntity
    private lateinit var pastis: VenueEntity

    @BeforeEach
    fun setUp() {
        this.initTestData()

        every { venueRepository.findByIdOrNull(1) } returns prettyBoyWingery
        every { venueRepository.findByIdOrNull(2) } returns pastis
        every { venueRepository.findByIdOrNull(3) } returns null
        every { venueRepository.findAll() } returns listOf(prettyBoyWingery, pastis)
        every { venueRepository.findByNameOrAddressOrPostalCodeOrCityOrNeighbourhood("%kaartinkaupunki%") } returns listOf(pastis)
        every { venueRepository.findByNameOrAddressOrPostalCodeOrCityOrNeighbourhood("%reykjavik%") } returns null
        every { venueMapper.venueEntityToVenueDTO(prettyBoyWingery) } returns prettyBoyWingeryDTO
    }

    @Test
    fun `getVenue returns a venue when queried ID exists`() {
        val venueId: Long = 1
        val expectedName: String = prettyBoyWingery.name
        val actualName: String? = venueService.getVenueNew(venueId)?.name

        verify(exactly = 1) { venueRepository.findByIdOrNull(venueId) };
        assertEquals(expectedName, actualName)
    }

    @Test
    fun `getVenue returns null when queried ID does not exist`() {
        val venueId: Long = 3
        val venueDTO: VenueDTO? = venueService.getVenueNew(venueId)

        verify(exactly = 1) { venueRepository.findByIdOrNull(venueId) };
        assertNull(venueDTO)
    }

    @Test
    fun `getAllVenus returns a list of venues`() {
        val expectedList = listOf(prettyBoyWingery, pastis)
        val actualList = venueService.getAllVenues()

        verify(exactly = 1) { venueRepository.findAll() }
        assertEquals(expectedList, actualList)
    }

    @Test
    fun `searchVenues returns a list of venues located in the queried neighbourhood `() {
        val query = "KAARTINKAUPUNKI"
        val expectedVenues = listOf(pastis)
        val actualVenue = venueService.searchVenues(query)

        assertEquals(expectedVenues, actualVenue)
    }

    @Test
    fun `Result of searchVenues does not contain a venue located in a different city`() {
        val query = "KAARTINKAUPUNKI"
        val venues = venueService.searchVenues(query)

        venues?.let { assertFalse(it.contains(prettyBoyWingery)) }
    }

    @Test
    fun `searchVenues return null if there is no venues that match the query`() {
        val query = "REYKJAVIK"
        val venues = venueService.searchVenues(query)

        assertNull(venues)
    }

    private fun initTestData() {
        prettyBoyWingery = VenueEntity(
            name = "Pretty Boy Wingery",
            streetAddress = "Piispansilta 11",
            postalCode = "02230",
            city = "Espoo",
            neighbourhood = Neighbourhood("Tapiola"),
        )

        prettyBoyWingeryDTO = VenueDTO(
            name = "Pretty Boy Wingery",
            streetAddress = "Piispansilta 11",
            postalCode = "02230",
            city = "Espoo",
            neighbourhood = NeighbourhoodDTO(name = "Tapiola"),
        )

        pastis = VenueEntity(
            name = "Pastis",
            streetAddress = "Pieni Roobertinkatu 2",
            postalCode = "00130",
            city = "Helsinki",
            neighbourhood = Neighbourhood("Kaartinkaupunki"),
        )
    }
}