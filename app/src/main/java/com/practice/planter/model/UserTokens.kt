package com.practice.planter.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserTokens(
    val session_token : String,
    val session_expiration : String,
    val refresh_token : String,
    val refresh_expiration : String,
)
