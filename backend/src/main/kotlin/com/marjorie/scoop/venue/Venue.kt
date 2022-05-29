package com.marjorie.scoop.venue

import com.fasterxml.jackson.annotation.JsonManagedReference
import com.marjorie.scoop.BaseEntity
import com.marjorie.scoop.neighbourhood.Neighbourhood
import com.marjorie.scoop.review.Review
import org.hibernate.annotations.Where
import org.jetbrains.annotations.Nullable
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * 'Venue' inherits BaseEntity's properties and functions.
 */
@Entity
@Table(name = "venue")
class Venue(
        @NotNull
        var name: String,

        @Nullable
        var description: String? = null,

        @Nullable
        @Column(name = "info_url")
        var infoUrl: String? = null,

        @Nullable
        @Column(name = "img_url")
        var imgUrl: String? = null,

        @NotNull
        @Size(max = 50)
        @Column(name = "street_address", length = 50)
        var streetAddress: String,

        @NotNull
        @Size(max = 50)
        @Column(name = "postal_code", length = 5)
        var postalCode: String,

        @NotNull
        @Size(max = 50)
        @Column(length = 50)
        var city: String,

        @Nullable
        @ManyToOne
        var neighbourhood: Neighbourhood? = null,

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "venue", cascade = [CascadeType.ALL])
        @Nullable
        @Where(clause = "deleted = false")
        @JsonManagedReference
        var reviewList: MutableList<Review>? = null
) : BaseEntity()