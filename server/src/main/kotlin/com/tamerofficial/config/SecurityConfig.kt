package com.tamerofficial.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain


@Configuration
@EnableWebFluxSecurity
class SecurityConfig {
    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain? {
        http.csrf()
            .disable()
            .authorizeExchange()
            .pathMatchers(HttpMethod.POST, "/employees/update")
            .hasRole("ADMIN")
            .pathMatchers("/hello")
            .permitAll()
            .pathMatchers("/actuator/**")
            .permitAll()
            .and()
            .formLogin()

        return http.build()
    }
}