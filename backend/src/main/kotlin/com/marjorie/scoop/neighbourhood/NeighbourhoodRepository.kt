package com.marjorie.scoop.neighbourhood

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * API for basic CRUD operations on 'Neighbourhood'
 */
@Repository
interface NeighbourhoodRepository: JpaRepository<Neighbourhood?, Long?>