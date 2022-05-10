package com.marjorie.scoop

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

/**
 * Base entity class with 'id', 'createdAt' and 'modifiedAt' properties.
 * Each mutable variable has a getter, setter and toString method
 * automatically created for it. Immutable variables have a setter and
 * toString method.
 */
@MappedSuperclass
abstract class BaseEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:CreationTimestamp
    val createdAt: Instant? = null,

    @field:UpdateTimestamp
    var modifiedAt: Instant? = null,
) {
    override fun toString(): String {
        return "id=$id, createdAt=$createdAt, modifiedAt=$modifiedAt"
    }

}