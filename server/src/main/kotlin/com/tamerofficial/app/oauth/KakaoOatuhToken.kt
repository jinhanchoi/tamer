package com.tamerofficial.app.oauth

data class KakaoOatuhToken(
    val access_token: String,
    val token_type: String,
    val refresh_token: String,
    val expires_in: Int,
    val refresh_token_expires_in: Int,
    val scope: String
) {
}