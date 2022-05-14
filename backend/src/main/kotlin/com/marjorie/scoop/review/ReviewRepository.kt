package com.marjorie.scoop.review

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * API for basic CRUD operations on 'Review'
 * */
@Repository
interface ReviewRepository : JpaRepository<Review?, Long?> {
}