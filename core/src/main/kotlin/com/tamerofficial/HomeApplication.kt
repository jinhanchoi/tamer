package com.tamerofficial

import com.tamerofficial.entity.MapData
import com.tamerofficial.domain.home.Place
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
class HomeApplication{

    suspend fun listNearByMe() : Flow<Place> {
        return Flux.fromIterable(listOf(
            Place(
            1,
            "test",
            "descTest",
            "https://www.naver.com",
            MapData(1,1),
            emptyList(),
            emptyList()
        )
        )).asFlow()
    }

}