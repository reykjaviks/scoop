package com.marjorie.scoop.neighbourhood

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * API for basic CRUD operations on Neighbourhood
 */
@Repository
interface NeighbourhoodRepository: JpaRepository<NeighbourhoodEntity?, Long?> {
    // todo: use a materialized view to calculate a random venue image representing a neighbourhood
    @Query("""
        with venue_counts as (
	        select 
		        neighbourhood_id, 
		        count(*) as venue_count
            from scoop.venue
            group by neighbourhood_id
        )
        select 
	        images.neighbourhood_name as neighbourhoodName, 
	        images.img_url as imgUrl, 
	        counts.venue_count as venueCount
            from scoop.neighbourhood_images images -- Materialized view containing image urls
            inner join venue_counts counts on counts.neighbourhood_id = images.neighbourhood_id
            order by counts.venue_count desc;
    """, nativeQuery = true)
    fun countVenuesByNeighbourhood(): List<NeighbourhoodAggregate>?
}