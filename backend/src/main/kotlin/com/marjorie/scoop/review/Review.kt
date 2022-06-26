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
        @Nullable
        var rating: Double,

        @Nullable
        var review: String,

        @ManyToOne(fetch = FetchType.LAZY)
        @NotNull
        @JoinColumn(name = "venue_id")
        @JsonBackReference
        var venue: Venue,

        @ManyToOne(fetch = FetchType.LAZY)
        @NotNull
        @JoinColumn(name = "user_id")
        @JsonBackReference
        var user: User,
): BaseEntity()