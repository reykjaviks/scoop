package com.marjorie.scoop.auth.userauthority

import com.fasterxml.jackson.annotation.JsonBackReference
import com.marjorie.scoop.common.BaseEntity
import com.marjorie.scoop.auth.authority.Authority
import com.marjorie.scoop.auth.user.User
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "userauthority")
class UserAuthority(
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    var user: User,

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authority_id")
    @JsonBackReference
    var authority: Authority,
): BaseEntity()