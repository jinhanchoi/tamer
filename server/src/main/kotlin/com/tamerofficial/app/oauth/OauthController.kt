package com.tamerofficial.app.oauth

import com.fasterxml.jackson.databind.JsonNode
import com.tamerofficial.common.Log
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.util.UriBuilder
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.lang.RuntimeException
import java.lang.StringBuilder
import java.net.URLEncoder


// 1. 사용자가 접근
// 2. 해당 플랫폼으로 리다이렉트( 필요한 파라미터 함께 전송 )
// 3. 사용자가 로그인 -> redirect url 설정한 화면이 보여지며, code 값 전달받음
// 4. 우리가 해당 플랫폼으로 받은 code & 파라미터(client_id, secret) 활용하여 POST
// 5. 유효하다면 access-token 을 받음
// 6. 받은 access token 으로 RS 로부터 정보를 가져옴

@Controller
class OauthController {

    companion object : Log

    @Autowired
    lateinit var webClient: WebClient

/************* Test Code *************
    // 사용자가 접근하는 상황
    // <로그인 버튼>을 누르면 아래의 정보들을 처리한 후
    // 카카오 플랫폼으로 redirect
//    @GetMapping("/oauth2/authorization/kakao")
    @GetMapping("/kakao")
    suspend fun kakaoLogin(): String {
        val params: Map<String, String> = mapOf(
            Pair("client_id", "72fb19cb6258b29fc4e77521b4ad9433"),
            Pair("redirect_uri", "http://localhost:8081/login/oauth2/code/kakao"),
            Pair("response_type", "code"),
            Pair("scope", "profile, account_email")
        )

        val encode: (String) -> String = { s -> URLEncoder.encode(s, Charsets.UTF_8)}

        fun requestUrl(params: Map<String, String>, encode: (String)-> String): String {
            return StringBuilder("https://kauth.kakao.com/oauth/authorize?")
                .append("client_id=").append(encode(params.getValue("client_id")))
                .append("&redirect_uri=").append(encode(params.getValue("redirect_uri")))
                .append("&response_type=code")
                .append("&scope=").append(encode(params.getValue("scope")))
                .toString()
        }

        return "redirect:" + requestUrl(params, encode)
    }

    // 사용자가 플랫폼에 로그인하면
    // 설정한 redirect_uri 로 code 를 받음
    @GetMapping("/login/oauth2/code/kakao")
    @ResponseBody
    suspend fun getKakaoCode(@RequestParam state: String): String {
        val params: MultiValueMap<String, String> = LinkedMultiValueMap()
        params.add("grant_type", "authorization_code")
        params.add("client_id", "72fb19cb6258b29fc4e77521b4ad9433")
        params.add("redirect_uri", "http://localhost:8081/login/oauth2/code/kakao")
        params.add("state", state)
        params.add("client_secret", "320gN6YS7c6bwb2dHtxvMqOcmOyhMFWE")

        // 받은 code 로 access_token 요청하기
        val accessTokenResult: Mono<KakaoOatuhToken> = webClient.mutate()
            .baseUrl("https://kauth.kakao.com/oauth/token")
            .build()
            .post()
            .uri("")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(params)
            .retrieve()
            .onStatus({ status -> status.is4xxClientError || status.is5xxServerError },
                { clientResponse ->
                    clientResponse.bodyToMono(String::class.java).map { body -> RuntimeException(body) }
                })
            .bodyToMono(KakaoOatuhToken::class.java)

        return "redirect:/login/kakao-access?access_token=" + accessTokenResult.awaitSingle().access_token
    }

    // 받은 access_token 으로 사용자 정보 받아오기
    @GetMapping("/login/kakao-access")
    @ResponseBody
    suspend fun getKakaoUserInfo(@RequestParam access_token: String): KakaoUserInfo {

        return webClient.mutate()
            .baseUrl("https://kapi.kakao.com/v2/user/me")
            .build()
            .post()
            .header(HttpHeaders.AUTHORIZATION, "Bearer $access_token")
            .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(KakaoUserInfo::class.java)
            .awaitSingle()
    }
 ************* Test Code *************/

/************* Test Code *************
    // google redirect uri
    @GetMapping("/login/oauth2/code/google")
    @ResponseBody
    suspend fun googleLogin(
        @RegisteredOAuth2AuthorizedClient authorizedClient: OAuth2AuthorizedClient,
        @AuthenticationPrincipal oAuth2User: OAuth2User,
        model: Model
    ): String {

        model.addAttribute("test", "test")
        return "forward:/api"
    }

    private fun testFun(authorizedClient: OAuth2AuthorizedClient): Flux<String> {
        return webClient
            .post()
            .uri(
                "https://www.googleapis.com"
            ) { uriBuilder ->
                uriBuilder
                    .path("/oauth2/v3/userinfo")
                    .build()
            }
            .attributes(oauth2AuthorizedClient(authorizedClient))
            .retrieve()
            .bodyToMono(object:ParameterizedTypeReference<List<JsonNode>>() {})
            .flatMapMany { Flux.fromIterable(it) }
            .map{jsonNode -> jsonNode.get("full_name").asText()}
    }
 ************* Test Code *************/


//    @GetMapping("/naver")

//    @GetMapping("/apple")

    @GetMapping("/api")
    @ResponseBody
    suspend fun afterLogin(
        @RegisteredOAuth2AuthorizedClient authorizedClient: OAuth2AuthorizedClient,
        @AuthenticationPrincipal oAuth2User: OAuth2User,
        model: Model
    ): ResponseEntity<Any> {

        val result: MutableMap<String, Any>? = oAuth2User.attributes

        return ResponseEntity.ok(result)

    }



}