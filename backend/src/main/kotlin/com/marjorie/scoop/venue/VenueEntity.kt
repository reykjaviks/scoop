package com.marjorie.scoop.venue

import com.fasterxml.jackson.annotation.JsonManagedReference
import com.marjorie.scoop.common.BaseEntity
import com.marjorie.scoop.neighbourhood.Neighbourhood
import com.marjorie.scoop.review.ReviewEntity
import org.hibernate.annotations.Where
import org.jetbrains.annotations.Nullable
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "venue")
class VenueEntity(
    @NotNull
    @Column(unique = true)
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
    @Column(name = "street_address")
    var streetAddress: String,

    @NotNull
    @Size(max = 5)
    @Column(name = "postal_code")
    var postalCode: String,

    @NotNull
    @Size(max = 50)
    var city: String,

    @Nullable
    @ManyToOne
    var neighbourhood: Neighbourhood? = null,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "venueEntity", cascade = [CascadeType.ALL])
    @Nullable
    @Where(clause = "deleted = false")
    @JsonManagedReference
    var reviewList: MutableList<ReviewEntity>? = ArrayList(),
): BaseEntity()