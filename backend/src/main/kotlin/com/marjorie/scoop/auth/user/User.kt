package com.marjorie.scoop.auth.user

import com.fasterxml.jackson.annotation.JsonManagedReference
import com.marjorie.scoop.BaseEntity
import com.marjorie.scoop.auth.authority.Authority
import com.marjorie.scoop.review.Review
import org.hibernate.annotations.Where
import org.jetbrains.annotations.Nullable
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

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
    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinTable(
        name = "userauthority",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "authority_id", referencedColumnName = "id")]
    )
    @JsonManagedReference
    var authorities: MutableList<Authority>?,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = [CascadeType.ALL])
    @Nullable
    @Where(clause = "deleted = false")
    @JsonManagedReference
    var reviewList: MutableList<Review>?,
 ): BaseEntity()