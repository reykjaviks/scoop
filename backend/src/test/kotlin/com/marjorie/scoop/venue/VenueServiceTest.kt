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
    val venueRepository: VenueRepository = mockk()
    val venueMapper: VenueMapper = mockk()
    val venueService = VenueService(venueRepository, venueMapper)

    lateinit var wingeryEntity: VenueEntity
    lateinit var pastisEntity: VenueEntity
    lateinit var wingeryDTO: VenueDTO
    lateinit var simpleWingeryDTO: SimpleVenueDTO
    lateinit var simplePastistDTO: SimpleVenueDTO

    @BeforeEach
    fun setUp() {
        this.initTestData()

        every { venueRepository.findByIdOrNull(1) } returns wingeryEntity
        every { venueRepository.findByIdOrNull(2) } returns pastisEntity
        every { venueRepository.findByIdOrNull(3) } returns null
        every { venueRepository.findAll() } returns listOf(wingeryEntity, pastisEntity)
        every { venueRepository.findByNameOrAddressOrPostalCodeOrCityOrNeighbourhood("%kaartinkaupunki%") } returns listOf(pastisEntity)
        every { venueRepository.findByNameOrAddressOrPostalCodeOrCityOrNeighbourhood("%reykjavik%") } returns null
        every { venueMapper.venueEntityToVenueDTO(wingeryEntity) } returns wingeryDTO
        every { venueMapper.venueEntitiesToSimpleVenueDTOs(listOf(pastisEntity)) } returns listOf(simplePastistDTO)
        every { venueMapper.venueEntitiesToSimpleVenueDTOs(listOf(wingeryEntity, pastisEntity)) } returns listOf(simpleWingeryDTO, simplePastistDTO)
    }

    @Test
    fun `getVenue returns a venue when queried ID exists`() {
        val venueId: Long = 1
        val expectedName: String = wingeryEntity.name
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
        val expectedList = listOf(simpleWingeryDTO, simplePastistDTO)
        val actualList = venueService.getAllVenues()

        verify(exactly = 1) { venueRepository.findAll() }
        assertEquals(expectedList, actualList)
    }

    @Test
    fun `searchVenues returns a list of venues located in the queried neighbourhood `() {
        val query = "KAARTINKAUPUNKI"
        val expectedVenues = listOf(simplePastistDTO)
        val actualVenues = venueService.searchVenues(query)

        assertEquals(expectedVenues, actualVenues)
    }

    @Test
    fun `Result of searchVenues does not contain a venue located in a different city`() {
        val query = "KAARTINKAUPUNKI"
        val venues = venueService.searchVenues(query)

        venues?.let { assertFalse(it.contains(simpleWingeryDTO)) }
    }

    @Test
    fun `searchVenues return null if there is no venues that match the query`() {
        val query = "REYKJAVIK"
        val venues = venueService.searchVenues(query)

        assertNull(venues)
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

        simpleWingeryDTO = SimpleVenueDTO(
            name = "Pretty Boy Wingery",
            streetAddress = "Piispansilta 11",
            postalCode = "02230",
            city = "Espoo",
        )

        simplePastistDTO = SimpleVenueDTO(
            name = "Pastis",
            streetAddress = "Pieni Roobertinkatu 2",
            postalCode = "00130",
            city = "Helsinki",
        )
    }
}