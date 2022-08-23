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
        with neighbourhood_images as (
	        select 
		        neighbourhood_id, 
		        img_url, 
    	        row_number() over (
    		        partition by neighbourhood_id order by random()
    	        ) as row_num
            from scoop.venue 
	        where neighbourhood_id is not null
            and img_url is not null
        ),
        neighbourhood_venues as (
	        select 
		        neighbourhood_id, 
		        count(*) as venues_count
            from scoop.venue
            group by neighbourhood_id
        )
        select 
	        nh.name as neighbourhoodName, 
	        images.img_url as imgLink, 
	        venues.venues_count as venueCount
            from neighbourhood_images images
            inner join neighbourhood_venues venues on venues.neighbourhood_id = images.neighbourhood_id
            inner join scoop.neighbourhood nh on nh.id = images.neighbourhood_id
            where images.row_num = 1
            order by venues.venues_count desc;
    """, nativeQuery = true)
    fun countVenuesByNeighbourhood(): List<NeighbourhoodAggregate>?
}