package com.tamerofficial.domain.home

import com.tamerofficial.entity.Comment
import com.tamerofficial.entity.MapData
import com.tamerofficial.entity.Review

data class Place(
    val placeId : Long,
    val name : String,
    val desc : String,
    val mainPicUrl : String,
    val mapData: MapData,
    val reviews : List<Review>,
    val comments : List<Comment>
)