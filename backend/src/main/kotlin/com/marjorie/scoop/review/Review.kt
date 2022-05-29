package com.marjorie.scoop.review

import com.fasterxml.jackson.annotation.JsonBackReference
import com.marjorie.scoop.BaseEntity
import com.marjorie.scoop.venue.Venue
import org.jetbrains.annotations.Nullable
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * 'Review' inherits the BaseEntity's properties and functions.
 */
@Entity
@Table(name = "review")
class Review(
        @NotNull
        @Column(name = "venue_id", updatable = false, insertable = false)
        var venueId: String,

        @Nullable
        @Column(name = "stars")
        var rating: Double,

        @Nullable
        @Column(name = "review")
        var review: String,

        @ManyToOne(fetch = FetchType.LAZY)
        @NotNull
        @JoinColumn(name = "venue_id")
        @JsonBackReference
        var venue: Venue,
) : BaseEntity()