package com.marjorie.scoop

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

/**
 * Base entity class with 'id', 'createdAt' and 'updatedAt' properties.
 * Each mutable variable has a getter and setter automatically created for it.
 */
@MappedSuperclass
abstract class BaseEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:CreationTimestamp
    val createdAt: Instant? = null,

    /* todo: implement in the database
    @field:UpdateTimestamp
    var updatedAt: Instant? = null,
    */
) {
    override fun toString(): String {
        return "id=$id, createdAt=$createdAt"
    }

}