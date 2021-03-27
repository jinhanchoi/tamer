package com.tamerofficial.app.home

import com.tamerofficial.common.ResponseEntity
import com.tamerofficial.domain.home.Home
import com.tamerofficial.domain.home.dto.Location
import com.tamerofficial.domain.home.dto.NearByPlace
import kotlinx.coroutines.flow.toList
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/home")
@RestController
class HomeApiController(private val home:Home) {

    @GetMapping
    suspend fun listNearByMe() : ResponseEntity<List<NearByPlace>> {
        return ResponseEntity(
            "200",
            "Success",
            home.listNearByPlace(Location(1,2)).toList()
        )
    }
}