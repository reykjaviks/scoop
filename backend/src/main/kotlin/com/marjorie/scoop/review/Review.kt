package com.marjorie.scoop.review

import com.fasterxml.jackson.annotation.JsonBackReference
import com.marjorie.scoop.common.BaseEntity
import com.marjorie.scoop.auth.user.User
import com.marjorie.scoop.venue.Venue
import org.jetbrains.annotations.Nullable
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * Review inherits the BaseEntity's properties and functions.
 */
@Entity
@Table(name = "review")
class Review(
        @NotNull
        var rating: Double,

        @NotNull
        var review: String,

        @NotNull
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "venue_id")
        @JsonBackReference
        var venue: Venue,

        @NotNull
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        @JsonBackReference
        var user: User,
): BaseEntity()