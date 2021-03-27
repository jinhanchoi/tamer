package com.tamerofficial.app.home

import com.tamerofficial.common.ResponseEntity
import com.tamerofficial.domain.home.Place
import kotlinx.coroutines.reactive.awaitFirst
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RequestMapping("/home")
@RestController
class HomeApiController {

    @GetMapping
    suspend fun listNearByMe() : ResponseEntity<List<Place>> {
        return ResponseEntity(
            "200",
            "Success",
            Flux.fromIterable(emptyList<Place>())
                .collectList()
                .awaitFirst()
        )
    }
}