package com.tamerofficial.domain.home

import org.springframework.stereotype.Component


interface Home {
    suspend fun listNearByPlace() : List<Place>
    suspend fun listRecommendKnowHow() : List<Any>
}

@Component
class HomeImpl : Home{
    override suspend fun listNearByPlace() : List<Place>{
        return emptyList()
    }

    override suspend fun listRecommendKnowHow() : List<Any> {
        return emptyList()
    }
}