package com.marjorie.scoop.auth.authority

import com.marjorie.scoop.BaseEntity
import com.marjorie.scoop.auth.user.User
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * 'Authority' inherits BaseEntity's properties and functions.
 */
@Entity
@Table(name = "authority")
class Authority(
    @NotNull
    var name: String,

    @JoinColumn(name = "user_id")
    @ManyToOne
    var user: User,
): BaseEntity()