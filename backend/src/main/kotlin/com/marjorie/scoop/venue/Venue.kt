package com.marjorie.scoop.venue

import com.marjorie.scoop.BaseEntity
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * Class 'Venue' inherits the BaseEntity's properties and functions
 * Variables and values defined in the constructor can be referenced using the property name, e.g. 'venue.name'
 */
@Entity
@Table(name = "venue")
class Venue(
        @NotNull
        @Column(name = "name")
        var name: String,
) : BaseEntity()