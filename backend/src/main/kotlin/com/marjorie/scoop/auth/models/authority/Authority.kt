package com.marjorie.scoop.auth.models.authority

import com.fasterxml.jackson.annotation.JsonBackReference
import com.marjorie.scoop.common.BaseEntity
import com.marjorie.scoop.auth.models.user.UserEntity
import org.jetbrains.annotations.Nullable
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "authority")
class Authority(
    @NotNull
    var name: String,

    @Nullable
    @ManyToMany(mappedBy = "authorities", fetch = FetchType.LAZY)
    @JsonBackReference
    var users: MutableList<UserEntity>? = null,
): BaseEntity()