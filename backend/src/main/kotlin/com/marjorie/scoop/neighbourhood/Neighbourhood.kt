package com.marjorie.scoop.neighbourhood

import com.marjorie.scoop.BaseEntity
import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@Table(name = "neighbourhood")
class Neighbourhood(
    @NotNull
    var name: String,
): BaseEntity()