package com.marjorie.scoop.venue

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * API for basic CRUD operations on 'Venue'
 * */
@Repository
interface VenueRepository : JpaRepository<Venue?, Long?> {
    fun findByName(name: String): List<Venue>
}