package com.tamerofficial.entity

import java.time.LocalDateTime

data class Comment(
    val content:String,
    val author:String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)