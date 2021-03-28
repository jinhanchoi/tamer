package com.tamerofficial.app.home

import com.ninjasquad.springmockk.MockkBean
import com.tamerofficial.common.ResponseEntity
import com.tamerofficial.config.SecurityConfig
import com.tamerofficial.domain.home.Home
import com.tamerofficial.domain.home.dto.Location
import com.tamerofficial.domain.home.dto.NearByPlace
import io.kotest.core.spec.style.FunSpec
import io.mockk.coEvery
import kotlinx.coroutines.flow.asFlow
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

@ExtendWith(SpringExtension::class)
@WebFluxTest(HomeApiController::class)
@Import(SecurityConfig::class)
class HomeControllerTest : FunSpec(){

    @MockkBean
    private lateinit var homeApp: Home

    @Autowired
    private lateinit var webClient : WebTestClient

    @Test
    fun `Home Api Should Return Results`(){
        coEvery{
            homeApp.listNearByPlace(any())
        }returns listOf<NearByPlace>(
                NearByPlace(
                    1L,
                    "난지천공원",
                    "차박안됨",
                    "www.naver.com",
                    Location(1,2),
                    emptyList(),
                    emptyList()
                )
            ).asFlow()

        val result = webClient.post()
            .uri("/home")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue("{\"lat\": 1,\"lon\":2}"))
            .exchange()
            .expectStatus().isOk
            .expectBody(object: ParameterizedTypeReference<ResponseEntity<List<NearByPlace>>>(){})
    }
}