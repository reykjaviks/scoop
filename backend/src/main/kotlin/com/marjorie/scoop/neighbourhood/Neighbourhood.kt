package com.marjorie.scoop.neighbourhood

import com.marjorie.scoop.BaseEntity
import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.NotNull

/**
 * 'Neighbourhood' inherits BaseEntity's properties and functions.
 */
@Entity
@Table(name = "neighbourhood")
class Neighbourhood(
    @NotNull
    var name: String,
) : BaseEntity()