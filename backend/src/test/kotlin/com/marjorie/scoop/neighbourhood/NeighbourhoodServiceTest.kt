package com.marjorie.scoop.neighbourhood

import com.marjorie.scoop.neighbourhood.dto.NeighbourhoodGetDTO
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class NeighbourhoodServiceTest {
    var neighbourhoodRepository: NeighbourhoodRepository = mockk()
    var neighbourhoodMapper: NeighbourhoodMapper = mockk()
    var neighbourhoodService: NeighbourhoodService = NeighbourhoodService(neighbourhoodRepository, neighbourhoodMapper)

    lateinit var kallioEntity: NeighbourhoodEntity
    lateinit var kluuviEntity: NeighbourhoodEntity
    lateinit var kallioDTO: NeighbourhoodGetDTO
    lateinit var kluuviDTO: NeighbourhoodGetDTO

    @BeforeEach
    fun setUp() {
        this.initTestData()
    }

    @Test
    fun `getNeighbourhood returns a neighbourhood when queried ID exists`() {
        val id: Long = 1

        every { neighbourhoodRepository.findByIdOrNull(id) } returns kluuviEntity
        every { neighbourhoodMapper.mapToNeighbourhoodGetDTO(kluuviEntity) } returns kluuviDTO

        val expectedNeighbourhoodDTO = kluuviDTO
        val actualNeighbourhoodDTO = neighbourhoodService.getNeighbourhood(id)

        verify(exactly = 1) { neighbourhoodRepository.findByIdOrNull(id) };
        assertEquals(expectedNeighbourhoodDTO, actualNeighbourhoodDTO)
    }

    @Test
    fun `getNeighbourhood returns null when queried ID does not exist`() {
        val id: Long = 1

        every { neighbourhoodRepository.findByIdOrNull(id) } returns null

        val result = neighbourhoodService.getNeighbourhood(id)

        verify(exactly = 1) { neighbourhoodRepository.findByIdOrNull(id) };
        assertNull(result)
    }

    @Test
    fun `getNeighbourhoodEntity returns entity when queried ID exists`() {
        val id: Long = 1

        every { neighbourhoodRepository.findByIdOrNull(id) } returns kluuviEntity

        val expectedNeighbourhoodEntity = kluuviEntity
        val actualNeighbourhoodEntity = neighbourhoodService.getNeighbourhoodEntity(id)

        verify(exactly = 1) { neighbourhoodRepository.findByIdOrNull(id) };
        assertEquals(expectedNeighbourhoodEntity, actualNeighbourhoodEntity)
    }

    @Test
    fun `getNeighbourhoodEntity returns null when queried ID does not exist`() {
        val id: Long = 1

        every { neighbourhoodRepository.findByIdOrNull(id) } returns null

        val result = neighbourhoodService.getNeighbourhoodEntity(id)

        verify(exactly = 1) { neighbourhoodRepository.findByIdOrNull(id) };
        assertNull(result)
    }

    @Test
    fun `getAllNeighbourhoods returns a list of neighbourhoods`() {
        every { neighbourhoodRepository.findAll() } returns listOf(kluuviEntity, kallioEntity)
        every { neighbourhoodMapper.mapToNeighbourhoodDTOs(any()) } returns listOf(kluuviDTO, kallioDTO)

        val expectedList = listOf(kluuviDTO, kallioDTO)
        val actualList = neighbourhoodService.getAllNeighbourhoods()

        verify(exactly = 1) { neighbourhoodRepository.findAll() }
        assertEquals(expectedList, actualList)
    }

    @Test
    fun `getAllNeighbourhoods returns null when neighbourhoods is empty`() {
        every { neighbourhoodRepository.findAll() } returns listOf()

        val result = neighbourhoodService.getAllNeighbourhoods()

        verify(exactly = 1) { neighbourhoodRepository.findAll() }
        assertNull(result)
    }

    private fun initTestData() {
        kallioEntity = NeighbourhoodEntity(name = "Kallio")
        kluuviEntity = NeighbourhoodEntity(name = "Kallio")
        kallioDTO = NeighbourhoodGetDTO(id = 1, name = "Kallio")
        kluuviDTO = NeighbourhoodGetDTO(id = 1, name = "Kluuvi")
    }
}