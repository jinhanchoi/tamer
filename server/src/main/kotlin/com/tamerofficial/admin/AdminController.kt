package com.tamerofficial.admin

import com.tamerofficial.Person
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AdminController {
    @GetMapping("/hello")
    suspend fun hello() : String{
        val j = Person("jinhan")
        return j.name
    }
}