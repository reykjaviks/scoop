package com.marjorie.scoop.neighbourhood

import com.marjorie.scoop.common.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@Table(name = "neighbourhood")
class NeighbourhoodEntity(
    @NotNull
    @Column(unique = true)
    var name: String,
): BaseEntity()