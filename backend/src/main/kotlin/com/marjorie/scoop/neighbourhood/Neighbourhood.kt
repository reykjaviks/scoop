package com.marjorie.scoop.neighbourhood

import com.marjorie.scoop.BaseEntity
import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.NotNull

/**
 * Entity 'Neighbourhood' inherits BaseEntity's properties and functions.
 * Variables and values defined in the constructor can be referenced using the
 * property name, e.g. 'neighbourhood.name'
 */
@Entity
@Table(name = "neighbourhood")
class Neighbourhood(
    @NotNull
    var name: String,
) : BaseEntity()