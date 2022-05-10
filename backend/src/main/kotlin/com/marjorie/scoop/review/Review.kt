package com.marjorie.scoop.review

import com.marjorie.scoop.BaseEntity
import org.jetbrains.annotations.Nullable
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * Class 'Review' inherits the BaseEntity's properties and functions
 * Variables and values defined in the constructor can be referenced using
 * the property name, e.g. 'review.rating'
 */
@Entity
@Table(name = "review")
class Review(
        @NotNull
        @Column(name = "venue_id")
        var venueId: String,

        @Nullable
        @Column(name = "stars")
        var rating: Double,

        @Nullable
        @Column(name = "review")
        var review: String,

        @NotNull
        @Column(name = "deleted")
        var deleted: Boolean,
) : BaseEntity()