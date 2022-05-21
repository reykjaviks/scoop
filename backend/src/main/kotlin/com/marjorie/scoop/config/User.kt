package com.marjorie.scoop.config

import com.marjorie.scoop.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "appuser")
class User (
    @NotNull
    @Column(name = "fullname")
    var name: String,

    @NotNull
    @Size(max = 50)
    var email: String,

    @NotNull
    var password: String,

    /*
    @Nullable
    @Type(type = "BigIntArray")
    @Column(columnDefinition = "bigint[]")
    var rolelist: Array<Long>? = arrayOf(),

    @Nullable
    @Type(type = "BigIntArray")
    @Column(columnDefinition = "bigint[]")
    var venuelist: Array<Long>? = arrayOf(),
    */

    @NotNull
    var islocked: Boolean,

) : BaseEntity()