package com.marjorie.scoop.auth.user

import com.fasterxml.jackson.annotation.JsonManagedReference
import com.marjorie.scoop.BaseEntity
import com.marjorie.scoop.auth.authority.Authority
import org.jetbrains.annotations.Nullable
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * 'User' inherits BaseEntity's properties and functions.
 */
@Entity
@Table(name = "user", schema = "scoop")
class User(
    @NotNull
    var name: String,

    @NotNull
    @Size(max = 50)
    @Column(name = "username")
    var username: String,

    @NotNull
    var password: String,

    @Nullable
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = [CascadeType.ALL])
    @JsonManagedReference
    var authorities: MutableList<Authority>?,
): BaseEntity()