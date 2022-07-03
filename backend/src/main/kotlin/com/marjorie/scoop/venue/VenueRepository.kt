package com.marjorie.scoop.venue

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

/**
 * API for basic CRUD operations on Venue
 */
@Repository
interface VenueRepository: JpaRepository<VenueEntity?, Long?> {
    @Query(
        """
            SELECT v FROM VenueEntity v
            LEFT JOIN Neighbourhood n ON n.id = v.neighbourhood.id
            WHERE LOWER(v.name) LIKE :query
            OR LOWER(v.streetAddress) LIKE :query
            OR LOWER(v.postalCode) LIKE :query
            OR LOWER(v.city) LIKE :query
            OR LOWER(n.name) LIKE :query
            """
    )
    fun findByNameOrAddressOrPostalCodeOrCityOrNeighbourhood(@Param("query") query: String): List<VenueEntity>?
    fun existsByName(name: String): Boolean
}