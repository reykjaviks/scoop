package com.marjorie.scoop.venue

import com.fasterxml.jackson.annotation.JsonManagedReference
import com.marjorie.scoop.BaseEntity
import com.marjorie.scoop.review.Review
import org.hibernate.annotations.Where
import org.jetbrains.annotations.Nullable
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * Class 'Venue' inherits the BaseEntity's properties and functions.
 * Variables and values defined in the constructor can be referenced using the property name, e.g. 'venue.name'
 */
@Entity
@Table(name = "venue")
class Venue(
        @NotNull
        @Column(name = "name")
        var name: String,

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "venue", cascade = [CascadeType.ALL])
        @Nullable
        @Where(clause = "deleted = false")
        @JsonManagedReference
        var reviewList: MutableList<Review>? = null
) : BaseEntity()