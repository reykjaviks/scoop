package com.marjorie.scoop.venue

import com.marjorie.scoop.neighbourhood.NeighbourhoodEntity
import com.marjorie.scoop.neighbourhood.dto.NeighbourhoodDTO
import com.marjorie.scoop.venue.dto.VenueDTONoReviews
import com.marjorie.scoop.venue.dto.VenueDTO
import com.marjorie.scoop.venue.dto.VenueDTOPost
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import java.time.Instant

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ContextConfiguration(classes = [VenueMapperImpl::class, ])
class VenueServiceTest {
    val venueRepository: VenueRepository = mockk()
    val venueMapper: VenueMapper = mockk()
    val venueService = VenueService(venueRepository, venueMapper)

    lateinit var wingeryEntity: VenueEntity
    lateinit var pastisEntity: VenueEntity
    lateinit var wingeryDTO: VenueDTO
    lateinit var wingeryDTONoReviews: VenueDTONoReviews
    lateinit var pastisDTONoReviews: VenueDTONoReviews
    lateinit var wingeryDtoPost: VenueDTOPost
    lateinit var pastisDtoPost: VenueDTOPost

    @BeforeEach
    fun setUp() {
        this.initTestData()
    }

    @Test
    fun `getVenue returns a venue when queried ID exists`() {
        every { venueRepository.findByIdOrNull(1) } returns wingeryEntity
        every { venueMapper.mapToVenueDTO(wingeryEntity) } returns wingeryDTO

        val expectedName: String = wingeryEntity.name
        val actualName: String? = venueService.getVenueDTO(1)?.name

        verify(exactly = 1) { venueRepository.findByIdOrNull(1) };
        assertEquals(expectedName, actualName)
    }

    @Test
    fun `getVenue returns null when queried ID does not exist`() {
        every { venueRepository.findByIdOrNull(2) } returns null

        val venueDTO = venueService.getVenueDTO(2)

        verify(exactly = 1) { venueRepository.findByIdOrNull(2) };
        assertNull(venueDTO)
    }

    @Test
    fun `getAllVenus returns a list of venues`() {
        every { venueRepository.findAll() } returns listOf(wingeryEntity, pastisEntity)
        every { venueMapper.mapToVenueDTONoReviewsList(listOf(wingeryEntity, pastisEntity)) } returns listOf(wingeryDTONoReviews, pastisDTONoReviews)

        val expectedList = listOf(wingeryDTONoReviews, pastisDTONoReviews)
        val actualList = venueService.getAllVenues()

        verify(exactly = 1) { venueRepository.findAll() }
        assertEquals(expectedList, actualList)
    }

    @Test
    fun `searchVenues returns a list of venues located in the queried neighbourhood `() {
        every { venueRepository.findByNameOrAddressOrPostalCodeOrCityOrNeighbourhood("%tapiola%") } returns listOf(wingeryEntity)
        every { venueMapper.mapToVenueDTONoReviewsList(listOf(wingeryEntity)) } returns listOf(wingeryDTONoReviews)

        val query = "TAPIOLA"
        val expectedVenues = listOf(wingeryDTONoReviews)
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
        every { venueMapper.mapToVenueEntity(wingeryDTONoReviews) } returns wingeryEntity
        every { venueMapper.mapToVenueDTO(wingeryEntity) } returns wingeryDTO

        val savedVenue = venueService.createVenue(wingeryDtoPost)
        val expectedVenue = wingeryDTO

        assertEquals(expectedVenue, savedVenue)
    }

    @Test
    fun `createVenue does not save venue`() {
        every { venueRepository.existsByName(wingeryEntity.name) } returns true

        val savedVenue = venueService.createVenue(wingeryDtoPost)

        assertNull(savedVenue)
    }

    @Test
    fun `updateVenue saves venue`() {
        every { venueRepository.findByIdOrNull(1) } returns wingeryEntity
        every { venueMapper.updateVenueEntity(wingeryDTONoReviews, wingeryEntity) } returns wingeryEntity
        every { venueMapper.mapToVenueDTO(wingeryEntity) } returns wingeryDTO

        val updatedVenue = venueService.updateVenue(1, wingeryDTONoReviews)
        val expectedVenue = wingeryDTO

        assertEquals(expectedVenue, updatedVenue)
    }

    @Test
    fun `updateVenue does not save venue`() {
        every { venueRepository.findByIdOrNull(1) } returns null

        val updatedVenue = venueService.updateVenue(1, wingeryDTONoReviews)

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

        wingeryDTONoReviews = VenueDTONoReviews(
            id = 1,
            name = "Pretty Boy Wingery",
            streetAddress = "Piispansilta 11",
            postalCode = "02230",
            city = "Espoo",
            createdAt = Instant.now(),
        )

        pastisDTONoReviews = VenueDTONoReviews(
            id = 2,
            name = "Pastis",
            streetAddress = "Pieni Roobertinkatu 2",
            postalCode = "00130",
            city = "Helsinki",
            createdAt = Instant.now(),
        )

        wingeryDtoPost = VenueDTOPost(
            name = "Pretty Boy Wingery",
            streetAddress = "Piispansilta 11",
            postalCode = "02230",
            city = "Espoo",
        )

        pastisDtoPost = VenueDTOPost(
            name = "Pastis",
            streetAddress = "Pieni Roobertinkatu 2",
            postalCode = "00130",
            city = "Helsinki",
        )
    }
}