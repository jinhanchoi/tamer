package com.tamerofficial.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("users")
data class UserEntity(
    @Id
    var id: Long? = null,
    var name: String? = null
)