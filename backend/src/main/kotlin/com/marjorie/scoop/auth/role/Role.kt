package com.marjorie.scoop.auth.role

import com.marjorie.scoop.BaseEntity
import com.marjorie.scoop.auth.user.User
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * 'Role' inherits BaseEntity's properties and functions.
 */
@Entity
@Table(name = "role")
class Role(
    @NotNull
    var name: String,

    @ManyToMany(mappedBy = "roles")
    var users: MutableList<User>?,
): BaseEntity()