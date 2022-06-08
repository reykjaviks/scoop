package com.marjorie.scoop.auth.userrole

import com.fasterxml.jackson.annotation.JsonBackReference
import com.marjorie.scoop.BaseEntity
import com.marjorie.scoop.auth.role.Role
import com.marjorie.scoop.auth.user.User
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * 'UserRole' inherits BaseEntity's properties and functions.
 */
@Entity
@Table(name = "userrole")
class UserRole(
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    var user: User,

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    @JsonBackReference
    var role: Role,
): BaseEntity()