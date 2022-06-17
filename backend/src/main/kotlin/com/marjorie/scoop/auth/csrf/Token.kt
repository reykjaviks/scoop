package com.marjorie.scoop.auth.csrf

import com.marjorie.scoop.BaseEntity
import org.jetbrains.annotations.Nullable
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "csrftoken", schema = "scoop")
class Token (
    @Nullable
    var identifier: String,

    @Nullable
    var value: String,
): BaseEntity()