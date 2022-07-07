package com.marjorie.scoop.venue

import com.marjorie.scoop.neighbourhood.Neighbourhood
import com.marjorie.scoop.neighbourhood.NeighbourhoodDTO
import com.marjorie.scoop.venue.dto.VenueDTONoReviews
import com.marjorie.scoop.venue.dto.VenueDTO
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
    val venueRepository: VenueRepository = mockk()
    val venueMapper: VenueMapper = mockk()
    val venueService = VenueService(venueRepository, venueMapper)

    lateinit var wingeryEntity: VenueEntity
    lateinit var pastisEntity: VenueEntity
    lateinit var wingeryDTO: VenueDTO
    lateinit var simpleWingeryDTO: VenueDTONoReviews
    lateinit var simplePastistDTO: VenueDTONoReviews

    @BeforeEach
    fun setUp() {
        this.initTestData()
    }

    @Test
    fun `getVenue returns a venue when queried ID exists`() {
        every { venueRepository.findByIdOrNull(1) } returns wingeryEntity
        every { venueMapper.venueEntityToVenueDTO(wingeryEntity) } returns wingeryDTO

        val expectedName: String = wingeryEntity.name
        val actualName: String? = venueService.getVenueNew(1)?.name

        verify(exactly = 1) { venueRepository.findByIdOrNull(1) };
        assertEquals(expectedName, actualName)
    }

    @Test
    fun `getVenue returns null when queried ID does not exist`() {
        every { venueRepository.findByIdOrNull(2) } returns null

        val venueDTO = venueService.getVenueNew(2)

        verify(exactly = 1) { venueRepository.findByIdOrNull(2) };
        assertNull(venueDTO)
    }

    @Test
    fun `getAllVenus returns a list of venues`() {
        every { venueRepository.findAll() } returns listOf(wingeryEntity, pastisEntity)
        every { venueMapper.venueEntitiesToVenueDTONoReviews(listOf(wingeryEntity, pastisEntity)) } returns listOf(simpleWingeryDTO, simplePastistDTO)

        val expectedList = listOf(simpleWingeryDTO, simplePastistDTO)
        val actualList = venueService.getAllVenues()

        verify(exactly = 1) { venueRepository.findAll() }
        assertEquals(expectedList, actualList)
    }

    @Test
    fun `searchVenues returns a list of venues located in the queried neighbourhood `() {
        every { venueRepository.findByNameOrAddressOrPostalCodeOrCityOrNeighbourhood("%tapiola%") } returns listOf(wingeryEntity)
        every { venueMapper.venueEntitiesToVenueDTONoReviews(listOf(wingeryEntity)) } returns listOf(simpleWingeryDTO)

        val query = "TAPIOLA"
        val expectedVenues = listOf(simpleWingeryDTO)
        val actualVenues = venueService.searchVenues(query)

        assertEquals(expectedVenues, actualVenues)
    }

    @Test
    fun `searchVenues returns null if there is no venues that match the query`() {
        every { venueRepository.findByNameOrAddressOrPostalCodeOrCityOrNeighbourhood("%reykjavik%") } returns null

        val query = "REYKJAVIK"
        val venues = venueService.searchVenues(query)

        assertNull(venues)
    }

    @Test
    fun `createVenue saves venue`() {
        every { venueRepository.existsByName(wingeryEntity.name) } returns false
        every { venueRepository.save(wingeryEntity) } returns wingeryEntity
        every { venueMapper.venueDTONoReviewsToVenueEntity(simpleWingeryDTO) } returns wingeryEntity
        every { venueMapper.venueEntityToVenueDTO(wingeryEntity) } returns wingeryDTO

        val savedVenue = venueService.createVenue(simpleWingeryDTO)
        val expectedVenue = wingeryDTO

        assertEquals(expectedVenue, savedVenue)
    }

    @Test
    fun `createVenue does not save venue`() {
        every { venueRepository.existsByName(wingeryEntity.name) } returns true

        val savedVenue = venueService.createVenue(simpleWingeryDTO)

        assertNull(savedVenue)
    }

    @Test
    fun `updateVenue saves venue`() {
        every { venueRepository.findByIdOrNull(1) } returns wingeryEntity
        every { venueMapper.updateVenueEntityFromVenueDTONoReviews(simpleWingeryDTO, wingeryEntity) } returns wingeryEntity
        every { venueMapper.venueEntityToVenueDTO(wingeryEntity) } returns wingeryDTO

        val updatedVenue = venueService.updateVenue(1, simpleWingeryDTO)
        val expectedVenue = wingeryDTO

        assertEquals(expectedVenue, updatedVenue)
    }

    @Test
    fun `updateVenue does not save venue`() {
        every { venueRepository.findByIdOrNull(1) } returns null

        val updatedVenue = venueService.updateVenue(1, simpleWingeryDTO)

        assertNull(updatedVenue)
    }

    private fun initTestData() {
        wingeryEntity = VenueEntity(
            name = "Pretty Boy Wingery",
            streetAddress = "Piispansilta 11",
            postalCode = "02230",
            city = "Espoo",
            neighbourhood = Neighbourhood("Tapiola"),
        )

        pastisEntity = VenueEntity(
            name = "Pastis",
            streetAddress = "Pieni Roobertinkatu 2",
            postalCode = "00130",
            city = "Helsinki",
            neighbourhood = Neighbourhood("Kaartinkaupunki"),
        )

        wingeryDTO = VenueDTO(
            name = "Pretty Boy Wingery",
            streetAddress = "Piispansilta 11",
            postalCode = "02230",
            city = "Espoo",
            neighbourhood = NeighbourhoodDTO(name = "Tapiola"),
        )

        simpleWingeryDTO = VenueDTONoReviews(
            name = "Pretty Boy Wingery",
            streetAddress = "Piispansilta 11",
            postalCode = "02230",
            city = "Espoo",
        )

        simplePastistDTO = VenueDTONoReviews(
            name = "Pastis",
            streetAddress = "Pieni Roobertinkatu 2",
            postalCode = "00130",
            city = "Helsinki",
        )
    }
}