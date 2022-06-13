package com.marjorie.scoop.auth.authority

import com.fasterxml.jackson.annotation.JsonBackReference
import com.marjorie.scoop.BaseEntity
import com.marjorie.scoop.auth.user.User
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "authority")
class Authority(
    @NotNull
    var name: String,

    @ManyToMany(mappedBy = "authorities", fetch = FetchType.LAZY)
    @JsonBackReference
    var users: MutableList<User>?,
): BaseEntity()