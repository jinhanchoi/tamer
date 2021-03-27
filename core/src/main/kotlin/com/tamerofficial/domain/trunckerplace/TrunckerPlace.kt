package com.tamerofficial.domain.trunckerplace

import com.tamerofficial.entity.Comment

data class TrunckerPlace(
    val title : String,
    val pictures : List<Picture>,
    val content : String,
    val comments : List<Comment>,
)

data class Picture(
    val url : String,
    val isMain : Boolean
)