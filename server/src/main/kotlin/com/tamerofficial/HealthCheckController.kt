package com.tamerofficial

import com.tamerofficial.entity.UserEntity
import com.tamerofficial.entity.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckController(private val userRepository: UserRepository) {
    //As Sample of usage of R2DBC with Coroutine and Webflux
    // Mono -> awaitSingle
    // Flux -> asFlow
    @GetMapping("/hello")
    suspend fun hello(): String {
        return "hello"
    }

    @GetMapping("/users")
    suspend fun sample(): Flow<UserEntity> {
        return userRepository.findAll().asFlow()
    }

}