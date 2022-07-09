package com.marjorie.scoop.venue

import com.marjorie.scoop.common.ScoopException
import com.marjorie.scoop.neighbourhood.NeighbourhoodEntity
import com.marjorie.scoop.neighbourhood.dto.NeighbourhoodDTO
import com.marjorie.scoop.venue.dto.VenueDTO
import com.marjorie.scoop.venue.dto.VenuePostDTO
import com.marjorie.scoop.venue.dto.VenueSearchDTO
import com.marjorie.scoop.venue.dto.VenueUpdateDTO
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import java.time.Instant
import kotlin.test.assertFailsWith

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ContextConfiguration(classes = [VenueMapperImpl::class, ])
class VenueServiceTest {
    val venueRepository: VenueRepository = mockk()
    val venueMapper: VenueMapper = mockk()
    val venueService = VenueService(venueRepository, venueMapper)

    lateinit var wingeryEntity: VenueEntity
    lateinit var pastisEntity: VenueEntity

    lateinit var wingeryDTO: VenueDTO

    lateinit var wingerySearchDTO: VenueSearchDTO
    lateinit var pastisSearchDTO: VenueSearchDTO

    lateinit var wingeryPostDTO: VenuePostDTO
    lateinit var passtisPostDTO: VenuePostDTO

    @BeforeEach
    fun setUp() {
        this.initTestData()
    }

    @Test
    fun `getVenue returns a venue when queried ID exists`() {
        val id: Long = 1

        every { venueRepository.findByIdOrNull(id) } returns wingeryEntity
        every { venueMapper.mapToVenueDTO(any()) } returns wingeryDTO

        val expectedName: String = wingeryEntity.name
        val actualName: String? = venueService.getVenueDTO(id)?.name

        verify(exactly = 1) { venueRepository.findByIdOrNull(id) };
        assertEquals(expectedName, actualName)
    }

    @Test
    fun `getVenue returns null when queried ID does not exist`() {
        val id: Long = 2

        every { venueRepository.findByIdOrNull(id) } returns null

        val venueDTO = venueService.getVenueDTO(id)

        verify(exactly = 1) { venueRepository.findByIdOrNull(id) };
        assertNull(venueDTO)
    }

    @Test
    fun `getAllVenus returns a list of venues`() {
        every { venueRepository.findAll() } returns listOf(wingeryEntity, pastisEntity)
        every { venueMapper.mapToVenueSearchDTOs(listOf(wingeryEntity, pastisEntity)) } returns listOf(wingerySearchDTO, pastisSearchDTO)

        val expectedList = listOf(wingerySearchDTO, pastisSearchDTO)
        val actualList = venueService.getAllVenues()

        verify(exactly = 1) { venueRepository.findAll() }
        assertEquals(expectedList, actualList)
    }

    @Test
    fun `searchVenues returns a list of venues located in the queried neighbourhood `() {
        every { venueRepository.findByNameOrAddressOrPostalCodeOrCityOrNeighbourhood("%tapiola%") } returns listOf(wingeryEntity)
        every { venueMapper.mapToVenueSearchDTOs(listOf(wingeryEntity)) } returns listOf(wingerySearchDTO)

        val query = "TAPIOLA"
        val expectedVenues = listOf(wingerySearchDTO)
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
        every { venueRepository.existsByName(any()) } returns false
        every { venueRepository.save(any()) } returns wingeryEntity
        every { venueMapper.mapToVenueDTO(any()) } returns wingeryDTO
        every { venueMapper.mapToVenueEntity(wingeryPostDTO) } returns wingeryEntity

        val savedVenue = venueService.createVenue(wingeryPostDTO)
        val expectedVenue = wingeryDTO

        assertEquals(expectedVenue, savedVenue)
    }

    @Test
    fun `createVenue does not save venue`() {
        every { venueRepository.existsByName(any()) } returns true

        assertFailsWith(
            exceptionClass = ScoopException::class,
            block = { venueService.createVenue(wingeryPostDTO) }
        )
    }

    @Test
    fun `updateVenue saves venue`() {
        every { venueRepository.findByIdOrNull(any()) } returns wingeryEntity
        every { venueMapper.updateVenueEntity(any(), any()) } returns wingeryEntity
        every { venueMapper.mapToVenueDTO(wingeryEntity) } returns wingeryDTO

        val updatedVenue = venueService.updateVenue(1, VenueUpdateDTO())
        val expectedVenue = wingeryDTO

        assertEquals(expectedVenue, updatedVenue)
    }

    @Test
    fun `updateVenue does not save venue`() {
        every { venueRepository.findByIdOrNull(any()) } returns null

        val updatedVenue = venueService.updateVenue(1, VenueUpdateDTO())

        assertNull(updatedVenue)
    }

    private fun initTestData() {
        wingeryEntity = VenueEntity(
            name = "Pretty Boy Wingery",
            streetAddress = "Piispansilta 11",
            postalCode = "02230",
            city = "Espoo",
            neighbourhood = NeighbourhoodEntity("Tapiola"),
        )

        pastisEntity = VenueEntity(
            name = "Pastis",
            streetAddress = "Pieni Roobertinkatu 2",
            postalCode = "00130",
            city = "Helsinki",
            neighbourhood = NeighbourhoodEntity("Kaartinkaupunki"),
        )

        wingeryDTO = VenueDTO(
            id = 1,
            name = "Pretty Boy Wingery",
            streetAddress = "Piispansilta 11",
            postalCode = "02230",
            city = "Espoo",
            neighbourhood = NeighbourhoodDTO(id = 1, name = "Tapiola"),
            createdAt = Instant.now(),
        )

        wingerySearchDTO = VenueSearchDTO(
            id = 1,
            name = "Pretty Boy Wingery",
            streetAddress = "Piispansilta 11",
            postalCode = "02230",
            city = "Espoo",
            createdAt = Instant.now(),
        )

        pastisSearchDTO = VenueSearchDTO(
            id = 2,
            name = "Pastis",
            streetAddress = "Pieni Roobertinkatu 2",
            postalCode = "00130",
            city = "Helsinki",
            createdAt = Instant.now(),
        )

        wingeryPostDTO = VenuePostDTO(
            name = "Pretty Boy Wingery",
            streetAddress = "Piispansilta 11",
            postalCode = "02230",
            city = "Espoo",
        )

        passtisPostDTO = VenuePostDTO(
            name = "Pastis",
            streetAddress = "Pieni Roobertinkatu 2",
            postalCode = "00130",
            city = "Helsinki",
        )
    }
}