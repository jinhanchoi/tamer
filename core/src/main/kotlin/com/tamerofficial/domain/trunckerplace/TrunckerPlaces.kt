package com.tamerofficial.domain.trunckerplace

import org.springframework.stereotype.Component

interface TrunckerPlaces{
    suspend fun hotPickOfWeek() : List<Picture>
    suspend fun listTruncking() : List<Any>
}

@Component
class TrunckerPlacesImpl : TrunckerPlaces{
    override suspend fun hotPickOfWeek(): List<Picture> {
        TODO("Not yet implemented")
    }

    override suspend fun listTruncking(): List<Any> {
        TODO("Not yet implemented")
    }
}