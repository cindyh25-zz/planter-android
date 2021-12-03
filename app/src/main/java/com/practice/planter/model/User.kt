package com.practice.planter.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val username : String,
    val password : String
)
